package at.moop.voicerecorder.database.repository

import at.moop.voicerecorder.model.Recording

/**
 * @author Markus Deutsch <markus@moop.at>
 */
interface RecordingRepository {

    fun getActiveRecording(callback: (recording: Recording?) -> Unit)

    fun deleteRecording(recording: Recording)

    fun storeRecording(recording: Recording)

}
