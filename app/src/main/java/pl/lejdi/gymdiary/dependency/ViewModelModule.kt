package pl.lejdi.gymdiary.dependency

import dagger.Module
import dagger.Provides
import pl.lejdi.gymdiary.database.repository.GymRepository

@Module
class ViewModelModule {
    @Provides
    fun provideRepository() : GymRepository
    {
        return GymRepository()
    }
}