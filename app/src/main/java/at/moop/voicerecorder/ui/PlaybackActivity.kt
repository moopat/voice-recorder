package at.moop.voicerecorder.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import at.moop.voicerecorder.R

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)

        val id = intent.getStringExtra(EXTRA_ID)
        if (id.isNullOrBlank()) {
            finish()
            return
        }


    }
}
