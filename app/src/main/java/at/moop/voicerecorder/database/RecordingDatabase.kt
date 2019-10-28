package at.moop.voicerecorder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.moop.voicerecorder.database.dao.RecordingDao
import at.moop.voicerecorder.model.Recording

/**
 * @author Markus Deutsch <markus@moop.at>
 */
@Database(entities = [Recording::class], version = 1)
@TypeConverters(Converters::class)
abstract class RecordingDatabase : RoomDatabase() {

    abstract fun getRecordingDao(): RecordingDao

    companion object {

        private const val databaseName = "recordings"

        fun buildDatabase(context: Context): RecordingDatabase {
            return Room.databaseBuilder(context, RecordingDatabase::class.java, databaseName)
                .build()
        }

    }

}
