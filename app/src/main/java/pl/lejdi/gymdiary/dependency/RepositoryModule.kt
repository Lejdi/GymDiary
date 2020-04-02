package pl.lejdi.gymdiary.dependency

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pl.lejdi.gymdiary.database.GymDatabase

@Module
class RepositoryModule {
    @Provides
    fun provideDatabase(application: Application) : GymDatabase
    {
        return Room.databaseBuilder(
            application,
            GymDatabase::class.java,
            GymDatabase.DB_NAME
        ).build()
    }
}