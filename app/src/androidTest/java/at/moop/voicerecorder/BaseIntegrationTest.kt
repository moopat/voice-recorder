package at.moop.voicerecorder

import androidx.test.platform.app.InstrumentationRegistry

/**
 * @author Markus Deutsch <markus@moop.at>
 */
open class BaseIntegrationTest {

    fun getContext() = InstrumentationRegistry.getInstrumentation().targetContext

    fun delay(millis: Long) {
        Thread.sleep(millis)
    }

}
