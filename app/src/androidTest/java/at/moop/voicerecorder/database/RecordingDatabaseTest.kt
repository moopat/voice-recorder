package at.moop.voicerecorder.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import at.moop.voicerecorder.model.Recording
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * @author Markus Deutsch <markus@moop.at>
 */
@RunWith(AndroidJUnit4::class)
class RecordingDatabaseTest {

    @Before
    @After
    fun clearDatabase(){
        RecordingDatabase.buildDatabase(getContext()).getRecordingDao().deleteAll()

    }

    @Test
    fun testDatabaseCreation() {
        val database = RecordingDatabase.buildDatabase(getContext())
        assertEquals(0, database.getRecordingDao().getAll().size)
    }

    @Test
    fun testRecordingInsertion() {
        val dao = RecordingDatabase.buildDatabase(getContext()).getRecordingDao()

        val recording = Recording(
            UUID.randomUUID().toString(),
            Date(),
            null
        )

        dao.insert(recording)
        val retrievedRecording = dao.getByUid(recording.uid)

        assertEquals(recording, retrievedRecording)
    }

    private fun getContext() = InstrumentationRegistry.getInstrumentation().targetContext

}
