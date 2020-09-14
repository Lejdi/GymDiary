package pl.lejdi.gymdiary.dependency

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import pl.lejdi.gymdiary.GymDiaryApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class
])

interface AppComponent : AndroidInjector<GymDiaryApplication>{
    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application : Application) : Builder

        fun build() : AppComponent
    }

    fun viewModelComponent(viewModelModule : ViewModelModule) : ViewModelComponent
    fun repositoryComponent(repositoryModule : RepositoryModule) : RepositoryComponent
}