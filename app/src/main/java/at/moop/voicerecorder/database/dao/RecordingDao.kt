package at.moop.voicerecorder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import at.moop.voicerecorder.model.Recording

/**
 * @author Markus Deutsch <markus@moop.at>
 */
@Dao
interface RecordingDao {

    @Query("SELECT * FROM recording ORDER BY startTime DESC")
    fun getAll(): List<Recording>

    @Query("SELECT * FROM recording ORDER BY startTime DESC")
    fun getAllLive(): LiveData<List<Recording>>

    @Query("SELECT * FROM recording WHERE uid = :uid ORDER BY startTime DESC")
    fun getByUid(uid: String): Recording?

    @Query("SELECT * FROM recording WHERE endTime IS NULL LIMIT 1")
    fun getActiveRecordingLive(): LiveData<Recording?>

    @Query("SELECT * FROM recording WHERE endTime IS NULL LIMIT 1")
    fun getActiveRecording(): Recording?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recording: Recording)

    @Delete
    fun delete(recording: Recording)

    @Query("DELETE FROM recording")
    fun deleteAll()

}
