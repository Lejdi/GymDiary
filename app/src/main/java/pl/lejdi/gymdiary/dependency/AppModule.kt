package pl.lejdi.gymdiary.dependency

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import pl.lejdi.gymdiary.database.repository.GymRepository
import pl.lejdi.gymdiary.ui.MainActivity
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideRepository(application: Application) : GymRepository
    {
        return GymRepository(application)
    }
}