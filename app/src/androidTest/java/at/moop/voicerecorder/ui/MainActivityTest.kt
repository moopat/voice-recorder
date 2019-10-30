package at.moop.voicerecorder.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import at.moop.voicerecorder.BaseIntegrationTest
import at.moop.voicerecorder.R
import at.moop.voicerecorder.database.RecordingDatabase
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


/**
 * @author Markus Deutsch <markus@moop.at>
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseIntegrationTest() {

    @get:Rule
    val rule = ActivityTestRule(MainActivity::class.java)

    // For the sake of the test we assume that the permission is granted.
    @get:Rule
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @Before
    @After
    fun clearDatabase() {
        RecordingDatabase.buildDatabase(getContext()).getRecordingDao().deleteAll()
    }

    @Test
    fun testRecordButtonIsShown() {
        onView(withContentDescription(R.string.main_button_toggle_cd)).check(matches(isDisplayed()))
    }

    @Test
    fun testToggleButtonClick() {
        // Default view
        onView(withId(R.id.tvStatus)).check(matches(withText("00:00")))

        // Click the button.
        onView(withId(R.id.buttonToggleRecording)).perform(click())

        delay(1000)

        onView(withId(R.id.tvStatus)).check(matches(not(withText("00:00"))))

        // Click again.
        onView(withId(R.id.buttonToggleRecording)).perform(click())

        onView(withId(R.id.tvStatus)).check(matches(withText("00:00")))
    }

    @Test
    fun testRecording() {

        // Start recording.
        onView(withId(R.id.buttonToggleRecording)).perform(click())

        delay(2000)

        // Get the id of the ongoing recording.
        val dao = RecordingDatabase.buildDatabase(getContext()).getRecordingDao()
        val recording = dao.getActiveRecording()
        assertNotNull(recording) // The recording should exist in the database.

        // Stop recording.
        onView(withId(R.id.buttonToggleRecording)).perform(click())
        delay(100)

        // There should be no active recording.
        assertNull(dao.getActiveRecording())

        // A recording file exists.
        assertTrue(File(getContext().filesDir, recording!!.uid).exists())
    }

}
