package at.moop.voicerecorder

import android.app.Application
import at.moop.voicerecorder.database.RecordingDatabase
import at.moop.voicerecorder.database.repository.RecordingRepository
import at.moop.voicerecorder.database.repository.RecordingRepositoryImpl
import at.moop.voicerecorder.viewmodel.MainActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class RecorderApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RecorderApplication)
            modules(module {
                single { RecordingDatabase.buildDatabase(androidContext()) }
                single { RecordingRepositoryImpl(get()) as RecordingRepository }
                viewModel { MainActivityViewModel(get()) }
            })
        }
    }

}
