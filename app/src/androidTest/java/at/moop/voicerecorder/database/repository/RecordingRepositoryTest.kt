package at.moop.voicerecorder.database.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import at.moop.voicerecorder.BaseIntegrationTest
import at.moop.voicerecorder.database.RecordingDatabase
import at.moop.voicerecorder.model.Recording
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author Markus Deutsch <markus@moop.at>
 */
@RunWith(AndroidJUnit4::class)
class RecordingRepositoryTest : BaseIntegrationTest() {

    @Before
    @After
    fun clearDatabase() {
        RecordingDatabase.buildDatabase(getContext()).getRecordingDao().deleteAll()
    }

    @Test
    fun testActiveRecordingReturnsNull() {
        val latch = CountDownLatch(1)

        val repository = getRepository()
        var didReturnRecording = false
        repository.getActiveRecording {
            assertNull(it)
            didReturnRecording = true
            latch.countDown()
        }

        latch.await(2, TimeUnit.SECONDS)
        assertTrue(didReturnRecording)
    }

    @Test
    fun testActiveRecordingReturnsRecording() {
        val latch = CountDownLatch(1)

        val repository = getRepository()
        val recording = Recording(UUID.randomUUID().toString(), Date(), null)
        repository.storeRecording(recording)

        delay(500) // Above method returns instantly, but work is done asynchronously

        var didReturnRecording = false
        repository.getActiveRecording {
            assertEquals(recording, it)
            didReturnRecording = true
            latch.countDown()
        }

        latch.await(2, TimeUnit.SECONDS)
        assertTrue(didReturnRecording)
    }

    @Test
    fun testGetExistingRecording() {
        val latch = CountDownLatch(1)

        val repository = getRepository()
        val recording = Recording(UUID.randomUUID().toString(), Date(), null)
        repository.storeRecording(recording)

        delay(500) // Above method returns instantly, but work is done asynchronously

        var didReturnRecording = false
        repository.getRecording(recording.uid) {
            assertEquals(recording, it)
            didReturnRecording = true
            latch.countDown()
        }

        latch.await(2, TimeUnit.SECONDS)
        assertTrue(didReturnRecording)
    }

    @Test
    fun testGetMissingRecording() {
        val latch = CountDownLatch(1)

        val repository = getRepository()

        var didReturnRecording = false
        repository.getRecording("doesNotExist") {
            assertNull(it)
            didReturnRecording = true
            latch.countDown()
        }

        latch.await(2, TimeUnit.SECONDS)
        assertTrue(didReturnRecording)
    }

    @Test
    fun testDeleteRecording() {
        var latch = CountDownLatch(1)

        val repository = getRepository()

        // Create a recording and insert it.
        val recording = Recording(UUID.randomUUID().toString(), Date(), null)
        repository.storeRecording(recording)

        delay(500) // Above method returns instantly, but work is done asynchronously

        // Assert that the recording exists.
        var didReturnRecording = false
        repository.getRecording(recording.uid) {
            assertEquals(recording, it)
            didReturnRecording = true
            latch.countDown()
        }
        latch.await(2, TimeUnit.SECONDS)
        assertTrue(didReturnRecording)

        // Delete the recording.
        repository.deleteRecording(recording)

        delay(500) // Above method returns instantly, but work is done asynchronously

        // Now try to get it.
        latch = CountDownLatch(1)
        didReturnRecording = false
        repository.getRecording(recording.uid) {
            assertNull(it)
            didReturnRecording = true
            latch.countDown()
        }

        latch.await(2, TimeUnit.SECONDS)
        assertTrue(didReturnRecording)
    }

    fun getRepository(): RecordingRepository {
        return RecordingRepositoryImpl(RecordingDatabase.buildDatabase(getContext()))
    }
}
