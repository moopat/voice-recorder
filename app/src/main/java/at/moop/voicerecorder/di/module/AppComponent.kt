package at.moop.voicerecorder.di.module

import at.moop.voicerecorder.RecorderApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * @author Markus Deutsch <markus@moop.at>
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<RecorderApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: RecorderApplication): AppComponent
    }
}
