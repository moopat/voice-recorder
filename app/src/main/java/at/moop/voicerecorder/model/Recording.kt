package at.moop.voicerecorder.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * @author Markus Deutsch <markus@moop.at>
 */
@Entity
data class Recording(
    @PrimaryKey val uid: String,
    val startTime: Date,
    var endTime: Date?
) {

    fun isRunning() = endTime == null

    // TODO: Let this method return the current recording duration for running recordings?
    fun getDuration(): Long? {

        endTime?.let {
            return it.time - startTime.time
        }

        return null
    }
}
