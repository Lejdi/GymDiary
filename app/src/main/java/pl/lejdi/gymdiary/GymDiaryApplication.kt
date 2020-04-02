package pl.lejdi.gymdiary

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import pl.lejdi.gymdiary.dependency.AppComponent
import pl.lejdi.gymdiary.dependency.DaggerAppComponent

class GymDiaryApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerAppComponent.builder().application(this).build()
        return component
    }

    companion object{
        lateinit var component : AppComponent
    }
}