package at.moop.voicerecorder.viewmodel

import androidx.lifecycle.ViewModel
import at.moop.voicerecorder.database.RecordingDatabase
import at.moop.voicerecorder.model.Recording
import java.util.*

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class MainActivityViewModel(private val database: RecordingDatabase) : ViewModel() {

    fun getActiveRecording() = database.getRecordingDao().getActiveRecordingLive()

    fun toggleRecording() {
        Thread(Runnable {
            if (database.getRecordingDao().getActiveRecording() == null) {
                startRecording()
            } else {
                endRecording()
            }
        }).start()
    }

    private fun startRecording() {
        Thread(Runnable {

            // Check if there's a recording already.
            if (database.getRecordingDao().getActiveRecording() != null) {
                return@Runnable // TODO: End it and start a new one?
            }

            database.getRecordingDao().insert(
                Recording(
                    UUID.randomUUID().toString(),
                    Date(),
                    null
                )
            )

        }).start()
    }

    private fun endRecording() {
        Thread(Runnable {

            val activeRecording = database.getRecordingDao().getActiveRecording() ?: return@Runnable

            activeRecording.endTime = Date()
            database.getRecordingDao().insert(activeRecording)

        }).start()
    }
}
