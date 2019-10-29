package at.moop.voicerecorder.viewmodel

import androidx.lifecycle.ViewModel
import at.moop.voicerecorder.database.RecordingDatabase

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class MainActivityViewModel(private val database: RecordingDatabase) : ViewModel() {

    fun getActiveRecording() = database.getRecordingDao().getActiveRecordingLive()

}
