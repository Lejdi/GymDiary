package pl.lejdi.gymdiary.dependency

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pl.lejdi.gymdiary.database.GymDatabase
import pl.lejdi.gymdiary.dependency.annotation.ApplicationScope

@Module
class RepositoryModule {
    @ApplicationScope
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