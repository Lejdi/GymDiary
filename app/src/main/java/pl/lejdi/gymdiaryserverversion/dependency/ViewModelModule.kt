package pl.lejdi.gymdiaryserverversion.dependency

import dagger.Module
import dagger.Provides
import pl.lejdi.gymdiaryserverversion.database.repository.GymRepository
import pl.lejdi.gymdiaryserverversion.dependency.annotation.ApplicationScope

@Module
class ViewModelModule {
    @ApplicationScope
    @Provides
    fun provideRepository() : GymRepository //inject repository to MainViewModel
    {
        return GymRepository()
    }
}