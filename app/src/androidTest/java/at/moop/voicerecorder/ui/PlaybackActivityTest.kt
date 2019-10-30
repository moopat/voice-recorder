package at.moop.voicerecorder.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import at.moop.voicerecorder.BaseIntegrationTest
import at.moop.voicerecorder.R
import at.moop.voicerecorder.database.RecordingDatabase
import at.moop.voicerecorder.formatAsDuration
import at.moop.voicerecorder.model.Recording
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.*

/**
 * @author Markus Deutsch <markus@moop.at>
 */
@RunWith(AndroidJUnit4::class)
class PlaybackActivityTest : BaseIntegrationTest() {

    companion object {
        const val FILE_NAME = "sample.wav"
        const val DURATION_IN_MILLIS = 5000
    }

    @get:Rule
    val rule = ActivityTestRule(PlaybackActivity::class.java, true, false)

    @After
    fun clearDatabase() {
        RecordingDatabase.buildDatabase(getContext()).getRecordingDao().deleteAll()
        File(getContext().filesDir, FILE_NAME).let {
            if (it.exists()) {
                it.delete()
            }
        }
    }

    @Before
    fun prepareFile() {
        // Copy file from assets.
        val targetFile = File(getContext().filesDir, FILE_NAME)

        InstrumentationRegistry.getInstrumentation().context.assets.open(FILE_NAME).use { fis ->
            targetFile.outputStream().use { fos ->
                fis.copyTo(fos)
            }
        }

        // Add database record.
        val recording = Recording(FILE_NAME, Date(), Date())
        RecordingDatabase.buildDatabase(getContext()).getRecordingDao().insert(recording)
    }

    @Test
    fun testPlayback() {

        // Launch the activity
        rule.launchActivity(PlaybackActivity.getIntent(getContext(), FILE_NAME))

        onView(withId(R.id.buttonTogglePlayback)).check(matches(isDisplayed()))

        // Start Playback
        onView(withId(R.id.buttonTogglePlayback)).perform(click())

        // Now the duration should read five seconds
        onView(withId(R.id.tvTotalDuration)).check(matches(withText(DURATION_IN_MILLIS.formatAsDuration())))

        // Playback is running
        assertTrue(rule.activity.mediaPlayer?.isPlaying == true)

        // Wait until the recording is over.
        delay(DURATION_IN_MILLIS.toLong())

        // Playback is done (null and false are okay)
        assertNotEquals(true, rule.activity.mediaPlayer?.isPlaying == true)
    }

    @Test
    fun testPlaybackStop() {

        // Launch the activity
        rule.launchActivity(PlaybackActivity.getIntent(getContext(), FILE_NAME))

        // Start Playback
        onView(withId(R.id.buttonTogglePlayback)).perform(click())

        // Playback is running
        assertTrue(rule.activity.mediaPlayer?.isPlaying == true)

        delay(1000)

        // Stop Playback
        onView(withId(R.id.buttonTogglePlayback)).perform(click())

        // Playback is not running (null and false are okay)
        assertNotEquals(true, rule.activity.mediaPlayer?.isPlaying == true)
        onView(withId(R.id.tvProgress)).check(matches(withText(0.formatAsDuration())))
    }

    @Test
    fun testDeletion() {

        // Launch the activity
        rule.launchActivity(PlaybackActivity.getIntent(getContext(), FILE_NAME))

        // Press delete.
        // Because of an issue with long-clicking in the test, we access the ViewModel directly
        rule.activity.model.deleteRecording(getContext())

        // Deletion is done asynchronously
        delay(200)

        assertNull(
            RecordingDatabase.buildDatabase(getContext()).getRecordingDao().getByUid(
                FILE_NAME
            )
        )
    }

}
