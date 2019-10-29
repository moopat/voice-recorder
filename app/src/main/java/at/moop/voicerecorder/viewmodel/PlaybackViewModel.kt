package at.moop.voicerecorder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import at.moop.voicerecorder.database.repository.RecordingRepository
import at.moop.voicerecorder.model.Recording

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class PlaybackViewModel(private val repository: RecordingRepository) : ViewModel() {

    lateinit var id: String
    val recording = MutableLiveData<Recording?>()
    val progress = MutableLiveData<Int>().apply { value = 0 }

    fun setRecordingId(id: String) {
        this.id = id
        repository.getRecording(id) {
            recording.postValue(it)
        }
    }

}
