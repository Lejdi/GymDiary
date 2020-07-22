package pl.lejdi.gymdiaryserverversion

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import pl.lejdi.gymdiaryserverversion.dependency.AppComponent
import pl.lejdi.gymdiaryserverversion.dependency.DaggerAppComponent

//application class - for using Dagger
class GymDiaryApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerAppComponent.builder().application(this).build()
        return component
    }

    companion object{
        lateinit var component : AppComponent
    }
}