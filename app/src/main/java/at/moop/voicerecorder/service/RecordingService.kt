package at.moop.voicerecorder.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import at.moop.voicerecorder.R
import at.moop.voicerecorder.database.repository.RecordingRepository
import at.moop.voicerecorder.model.Recording
import org.koin.android.ext.android.inject
import java.util.*

class RecordingService : Service() {

    val repository: RecordingRepository by inject()
    val tickHandler = Handler()
    val tickRunnable = TickRunnable(tickHandler)

    var recording: Recording? = null
    var isRecording = false

    companion object {

        const val SERVICE_ID = 1

        @JvmStatic
        fun toggleRecording(context: Context) {
            val intent = Intent(context, RecordingService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(SERVICE_ID, getNotification())

        toggleRecording()
        return START_STICKY
    }

    private fun toggleRecording() {
        repository.getActiveRecording {
            if (it == null) {
                startRecording()
            } else if (isRecording) {
                stopRecording()
            } else {
                // Inconsistency possibly caused by a crash:
                // There is an open recording, but we are not recording at the moment.
                // Remove the open recording.
                repository.deleteRecording(it)
                stopSelf()
            }
        }
    }

    private fun startRecording() {
        isRecording = true

        recording = Recording(
            UUID.randomUUID().toString(),
            Date(),
            null
        ).apply {
            repository.storeRecording(this)
        }

        startTicking()

    }

    private fun stopRecording() {
        isRecording = false

        recording?.let {
            it.endTime = Date()
            repository.storeRecording(it)
        }

        stopSelf()
    }

    override fun onDestroy() {
        stopTicking()
        super.onDestroy()
    }

    private fun getNotification(): Notification {
        val builder = NotificationCompat.Builder(this, createDefaultNotificationChannel())
            .setSmallIcon(R.drawable.ic_stat_mic_none)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(recording?.getDuration(true)?.toString() ?: "---")
            .setOngoing(true)

        return builder.build()
    }

    private fun startTicking() {
        tickHandler.post(tickRunnable)
    }

    private fun stopTicking() {
        tickHandler.removeCallbacksAndMessages(null)
    }

    private fun createDefaultNotificationChannel(): String {
        val id = "recording"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(id, name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
        return id
    }

    override fun onBind(intent: Intent): IBinder {
        throw NotImplementedError()
    }

    inner class TickRunnable(private val handler: Handler) : Runnable {

        override fun run() {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(SERVICE_ID, getNotification())
            handler.postDelayed(this, 1000)
        }

    }
}
