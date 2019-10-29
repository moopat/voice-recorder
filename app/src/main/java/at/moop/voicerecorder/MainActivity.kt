package at.moop.voicerecorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import at.moop.voicerecorder.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    val model: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.getActiveRecording().observe(this, Observer {
            if (it == null) {
                tvStatus.setText(R.string.main_status_idle)
            } else {
                tvStatus.setText(R.string.main_status_recording)
            }
        })

        buttonToggleRecording.setOnClickListener {
            model.toggleRecording()
        }
    }
}
