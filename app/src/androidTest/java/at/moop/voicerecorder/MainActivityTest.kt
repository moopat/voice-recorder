package at.moop.voicerecorder

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
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

    @Test
    fun testRecordButtonIsShown() {
        onView(withContentDescription(R.string.main_button_toggle_cd)).check(matches(isDisplayed()))
    }


}
