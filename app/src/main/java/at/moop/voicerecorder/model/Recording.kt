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

    fun getDuration(ongoing: Boolean = true): Long? {

        val endTimeForCalculation = endTime ?: if(ongoing) Date() else null

        endTimeForCalculation?.let {
            return it.time - startTime.time
        }

        return null
    }
}
