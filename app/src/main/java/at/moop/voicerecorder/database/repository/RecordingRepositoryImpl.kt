package at.moop.voicerecorder.database.repository

import at.moop.voicerecorder.database.RecordingDatabase
import at.moop.voicerecorder.model.Recording

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class RecordingRepositoryImpl(private val database: RecordingDatabase) : RecordingRepository {

    override fun storeRecording(recording: Recording) {
        Thread(Runnable {
            database.getRecordingDao().insert(recording)
        }).start()
    }

    override fun deleteRecording(recording: Recording) {
        Thread(Runnable {
            database.getRecordingDao().delete(recording)
        }).start()
    }

    override fun getActiveRecording(callback: (recording: Recording?) -> Unit) {
        Thread(Runnable {
            callback.invoke(database.getRecordingDao().getActiveRecording())
        }).start()
    }

}