package at.moop.voicerecorder

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import at.moop.voicerecorder.database.RecordingDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * @author Markus Deutsch <markus@moop.at>
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val rule = ActivityTestRule(MainActivity::class.java)

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
        // First "idle" is shown.
        onView(withId(R.id.tvStatus)).check(matches(withText(R.string.main_status_idle)))

        // Click the button.
        onView(withId(R.id.buttonToggleRecording)).perform(click())

        // Now "recording" is shown.
        onView(withId(R.id.tvStatus)).check(matches(withText(R.string.main_status_recording)))

        // Click again.
        onView(withId(R.id.buttonToggleRecording)).perform(click())

        // Idle is shown again.
        onView(withId(R.id.tvStatus)).check(matches(withText(R.string.main_status_idle)))
    }

    private fun getContext() = InstrumentationRegistry.getInstrumentation().targetContext

}
