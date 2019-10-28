package at.moop.voicerecorder.di.module

import android.app.Application
import android.content.Context
import at.moop.voicerecorder.database.RecordingDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Markus Deutsch <markus@moop.at>
 */
@Module
class AppModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): RecordingDatabase {
        return RecordingDatabase.buildDatabase(context)
    }

}
