package at.moop.voicerecorder.viewmodel

import androidx.lifecycle.ViewModel
import at.moop.voicerecorder.database.repository.RecordingRepository

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class RecordingsViewModel(repository: RecordingRepository) : ViewModel() {

    val recordings = repository.getAllRecordings()

}
