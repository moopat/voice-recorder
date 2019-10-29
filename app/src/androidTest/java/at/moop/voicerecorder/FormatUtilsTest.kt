package at.moop.voicerecorder

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * @author Markus Deutsch <markus@moop.at>
 */
@RunWith(AndroidJUnit4::class)
class FormatUtilsTest {

    @Test
    fun testFormatAsDuration() {
        assertEquals("00:00", 0.formatAsDuration())
        assertEquals("00:01", 1000.formatAsDuration())
        assertEquals("01:00", TimeUnit.MINUTES.toMillis(1).formatAsDuration())
        assertEquals("1:00:00", TimeUnit.HOURS.toMillis(1).formatAsDuration())
        assertEquals("12:00:00", TimeUnit.HOURS.toMillis(12).formatAsDuration())
    }

}
