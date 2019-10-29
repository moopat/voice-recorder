package at.moop.voicerecorder.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import at.moop.voicerecorder.R
import at.moop.voicerecorder.service.RecordingService
import at.moop.voicerecorder.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    companion object {
        const val RC_PERMISSION = 1
    }

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
            if (hasRecordingPermission()) {
                toggleRecording()
            } else {
                requestRecordingPermission()
            }
        }

        buttonShowAll.setOnClickListener {
            startActivity(Intent(this, RecordingsActivity::class.java))
        }
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
            // TODO: Remember that the permission was denied.
        }
    }
}
