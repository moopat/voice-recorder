package at.moop.voicerecorder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import at.moop.voicerecorder.R
import at.moop.voicerecorder.adapter.RecordingsAdapter
import at.moop.voicerecorder.viewmodel.RecordingsViewModel
import kotlinx.android.synthetic.main.activity_recordings.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecordingsActivity : AppCompatActivity() {

    lateinit var adapter: RecordingsAdapter
    val model: RecordingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recordings)

        adapter = RecordingsAdapter {
            startActivity(PlaybackActivity.getIntent(this, it.uid))
        }
        recyclerView.adapter = adapter

        model.recordings.observe(this, Observer {
            adapter.setData(it)
        })

    }
}
