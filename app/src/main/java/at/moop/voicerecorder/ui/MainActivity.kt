package at.moop.voicerecorder.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import at.moop.voicerecorder.R
import at.moop.voicerecorder.formatAsDuration
import at.moop.voicerecorder.service.RecordingService
import at.moop.voicerecorder.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    companion object {
        const val RC_PERMISSION = 1
    }

    val model: MainActivityViewModel by viewModel()
    val tickHandler = Handler()
    val receiver = AmplitudeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.activeRecording.observe(this, Observer {
            if (it == null) {
                buttonToggleRecording.backgroundTintList =
                    ColorStateList.valueOf(getColor(R.color.colorStateIdle))
            } else {
                buttonToggleRecording.backgroundTintList =
                    ColorStateList.valueOf(getColor(R.color.colorStateRecording))
            }
        })

        buttonToggleRecording.setOnClickListener {
            if (hasRecordingPermission()) {
                toggleRecording()
            } else {
                requestRecordingPermission()
            }
        }

        buttonShowAll.setOnClickListener {
            startActivity(Intent(this, RecordingsActivity::class.java))
        }

        buttonGrantPermission.setOnClickListener {
            startActivity(Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            })
        }
    }

    private fun updateDuration() {
        tvStatus.text =
            (model.activeRecording.value?.getDuration(true) ?: 0).formatAsDuration()
    }

    override fun onResume() {
        super.onResume()
        startTicking()
        registerReceiver(receiver, IntentFilter(RecordingService.BROADCAST_AMPLITUDE))

        // If the user has denied the mic permission, we need to hide the record button
        updateButtonVisibilities()
    }

    override fun onPause() {
        super.onPause()
        stopTicking()
        unregisterReceiver(receiver)
    }

    private fun updateButtonVisibilities() {
        if (model.isPermissionDenied() && !hasRecordingPermission()) {
            buttonGrantPermission.visibility = View.VISIBLE
            buttonToggleRecording.visibility = View.GONE
            tvStatus.visibility = View.GONE
        } else {
            buttonGrantPermission.visibility = View.GONE
            buttonToggleRecording.visibility = View.VISIBLE
            tvStatus.visibility = View.VISIBLE
        }
    }

    private fun startTicking() {
        tickHandler.post(TickRunnable())
    }

    private fun stopTicking() {
        tickHandler.removeCallbacksAndMessages(null)
    }

    private fun toggleRecording() {
        RecordingService.toggleRecording(this)
    }

    private fun hasRecordingPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestRecordingPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            RC_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (hasRecordingPermission()) {
            toggleRecording()
        } else {
            model.setPermissionDenied(true)
        }
    }

    inner class TickRunnable : Runnable {
        override fun run() {
            runOnUiThread {
                updateDuration()
                tickHandler.postDelayed(this, 100)
            }
        }

    }

    inner class AmplitudeReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val amplitude = intent?.getIntExtra(RecordingService.EXTRA_AMPLITUDE, 0) ?: 0
            bouncingCircle.setRadiusRatio(amplitude.toFloat().div(1000))
        }

    }
}
