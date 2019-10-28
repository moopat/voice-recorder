package at.moop.voicerecorder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import at.moop.voicerecorder.model.Recording

/**
 * @author Markus Deutsch <markus@moop.at>
 */
@Dao
interface RecordingDao {

    @Query("SELECT * FROM recording ORDER BY startTime DESC")
    fun getAll(): List<Recording>

    @Query("SELECT * FROM recording WHERE uid = :uid ORDER BY startTime DESC")
    fun getByUid(uid: String): Recording?

    @Query("SELECT * FROM recording WHERE endTime IS NULL LIMIT 1")
    fun getActiveRecording(): LiveData<Recording?>

    @Insert
    fun insert(recording: Recording)

    @Delete
    fun delete(recording: Recording)

}
