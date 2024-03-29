package at.moop.voicerecorder.ui

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.AnimatedVectorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import at.moop.voicerecorder.R
import at.moop.voicerecorder.formatAsDuration
import at.moop.voicerecorder.getColor
import at.moop.voicerecorder.viewmodel.PlaybackViewModel
import kotlinx.android.synthetic.main.activity_playback.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaybackActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_ID = "id"

        @JvmStatic
        fun getIntent(context: Context, id: String): Intent {
            return Intent(context, PlaybackActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
            }
        }

    }

    val model: PlaybackViewModel by viewModel()
    var mediaPlayer: MediaPlayer? = null
    private var seekbarUpdateHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)

        val id = intent.getStringExtra(EXTRA_ID)
        if (id.isNullOrBlank()) {
            finish()
            return
        }

        model.recording.observe(this, Observer {
            if (it == null) {
                finish()
                return@Observer
            }
            tvTotalDuration.text = it.getDuration(false)?.formatAsDuration()
            ColorStateList.valueOf(it.getColor()).let { csl ->
                buttonTogglePlayback.backgroundTintList = csl
                seekBar.thumbTintList = csl
            }
        })

        model.progress.observe(this, Observer {
            seekBar.setProgress(it, true)
            tvProgress.text = it.formatAsDuration()
        })

        buttonTogglePlayback.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                stopPlayback()
            } else {
                startPlayback()
            }
        }

        buttonDelete.setOnLongClickListener {
            model.deleteRecording(this)
            finish()
            true
        }

        seekbarUpdateHandler.post(UpdateSeekbarRunnable())
        model.setRecordingId(id)
    }

    private fun startPlayback() {
        val mediaFile = File(filesDir, model.id)
        if (!mediaFile.exists()) {
            Log.e("PlaybackActivity", "Media file cannot be found.")
            return
        }

        (buttonTogglePlayback.drawable as AnimatedVectorDrawable).start()

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setOnCompletionListener {
                (buttonTogglePlayback.drawable as AnimatedVectorDrawable).reset()
                model.progress.postValue(0)
            }
            setDataSource(mediaFile.path)
            prepare()
            start()
        }
        seekBar.max = mediaPlayer?.duration ?: 0
        tvTotalDuration.text = mediaPlayer?.duration?.formatAsDuration()
    }

    private fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        model.progress.postValue(0)
        (buttonTogglePlayback.drawable as AnimatedVectorDrawable).reset()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        seekbarUpdateHandler.removeCallbacksAndMessages(null)
    }

    inner class UpdateSeekbarRunnable : Runnable {
        override fun run() {
            model.progress.postValue(mediaPlayer?.currentPosition ?: 0)
            seekbarUpdateHandler.postDelayed(this, 30)
        }

    }
}
