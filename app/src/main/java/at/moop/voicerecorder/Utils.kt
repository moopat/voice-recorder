package at.moop.voicerecorder

import android.graphics.Color
import at.moop.voicerecorder.model.Recording
import java.util.concurrent.TimeUnit

/**
 * @author Markus Deutsch <markus@moop.at>
 */

fun Long.formatAsDuration(): String {
    var remainder = this
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    remainder -= TimeUnit.HOURS.toMillis(hours)

    val minutes = TimeUnit.MILLISECONDS.toMinutes(remainder)
    remainder -= TimeUnit.MINUTES.toMillis(minutes)

    val seconds = TimeUnit.MILLISECONDS.toSeconds(remainder)

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

fun Int.formatAsDuration(): String {
    return toLong().formatAsDuration()
}

fun Recording.getHue(): Float {
    val duration = getDuration(true) ?: 0
    return duration.rem(70).toFloat()
}

fun Recording.getColor(): Int {
    return Color.HSVToColor(
        floatArrayOf(
            getHue(), 0.8f, 0.9f
        )
    )
}
