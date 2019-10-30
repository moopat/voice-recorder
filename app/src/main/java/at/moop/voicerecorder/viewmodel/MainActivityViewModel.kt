package at.moop.voicerecorder.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import at.moop.voicerecorder.database.RecordingDatabase

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class MainActivityViewModel(context: Context, private val database: RecordingDatabase) :
    ViewModel() {

    companion object {
        private const val PREFERENCE_NAME = "default"
        private const val PREF_PERMISSION_DENIED = "permission_denied"
    }

    private val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    val activeRecording = database.getRecordingDao().getActiveRecordingLive()

    fun setPermissionDenied(denied: Boolean) {
        preferences.edit().putBoolean(PREF_PERMISSION_DENIED, denied).apply()
    }

    fun isPermissionDenied() = preferences.getBoolean(PREF_PERMISSION_DENIED, false)

}
