package at.moop.voicerecorder.ui

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import at.moop.voicerecorder.BaseIntegrationTest
import at.moop.voicerecorder.R
import at.moop.voicerecorder.database.RecordingDatabase
import at.moop.voicerecorder.model.Recording
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


/**
 * @author Markus Deutsch <markus@moop.at>
 */
@RunWith(AndroidJUnit4::class)
class RecordingsActivityTest : BaseIntegrationTest() {

    private val NUMBER_OF_ITEMS = 3

    @get:Rule
    val rule = ActivityTestRule(RecordingsActivity::class.java, true, false)

    @After
    fun clearDatabase() {
        RecordingDatabase.buildDatabase(getContext()).getRecordingDao().deleteAll()
    }

    @Before
    fun prepareData() {
        val dao = RecordingDatabase.buildDatabase(getContext()).getRecordingDao()
        dao.deleteAll()
        Log.d("RecordingsActivityTest", "Preparing data...")
        for (i in 1..NUMBER_OF_ITEMS) {
            Log.d("RecordingsActivityTest", "Inserting element $i")
            dao.insert(Recording(UUID.randomUUID().toString(), Date(), Date()))
        }
    }

    @Test
    fun testListHasElements() {
        rule.launchActivity(Intent(getContext(), RecordingsActivity::class.java))
        val recyclerView = rule.activity.findViewById<RecyclerView>(R.id.recyclerView)
        assertNotNull(recyclerView)
        delay(200)
        assertEquals(NUMBER_OF_ITEMS, recyclerView.adapter?.itemCount)
    }

    @Test
    fun testTapOpensPlayback() {
        rule.launchActivity(Intent(getContext(), RecordingsActivity::class.java))
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.buttonTogglePlayback)).check(matches(isDisplayed()))
    }

}
