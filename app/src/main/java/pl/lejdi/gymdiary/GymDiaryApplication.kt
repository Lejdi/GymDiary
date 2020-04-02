package pl.lejdi.gymdiary

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import pl.lejdi.gymdiary.dependency.DaggerAppComponent

class GymDiaryApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}