package pl.lejdi.gymdiary.dependency

import dagger.Module
import dagger.Provides
import pl.lejdi.gymdiary.database.repository.GymRepository
import pl.lejdi.gymdiary.dependency.annotation.ApplicationScope

@Module
class ViewModelModule {
    @ApplicationScope
    @Provides
    fun provideRepository() : GymRepository //inject repository to MainViewModel
    {
        return GymRepository()
    }
}