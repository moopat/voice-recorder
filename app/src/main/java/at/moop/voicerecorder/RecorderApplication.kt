package at.moop.voicerecorder

import at.moop.voicerecorder.di.module.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class RecorderApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

}
