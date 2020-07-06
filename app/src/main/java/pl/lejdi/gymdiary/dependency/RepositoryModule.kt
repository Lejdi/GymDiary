package pl.lejdi.gymdiary.dependency

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        val db = Room.databaseBuilder(
            application,
            GymDatabase::class.java,
            GymDatabase.DB_NAME
        )
        db.addMigrations(MIGRATION_6_7)
        return db.build()
    }

    private val MIGRATION_6_7 = object : Migration(6,7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("PRAGMA foreign_keys=off;")
            database.execSQL("CREATE TABLE sets_backup(" +
                    "id INTEGER NOT NULL PRIMARY KEY, " +
                    "trainingid INTEGER NOT NULL, " +
                    "exercisename TEXT NOT NULL, " +
                    "repetitions INTEGER NOT NULL, " +
                    "weight REAL NOT NULL, " +
                    "rv_position INTEGER NOT NULL, " +
                    "FOREIGN KEY(trainingid) REFERENCES trainings(id) ON DELETE CASCADE," +
                    "FOREIGN KEY(exercisename) REFERENCES exercises(name) ON DELETE CASCADE" +
                    ");")
            database.execSQL("INSERT INTO sets_backup SELECT id, trainingid, exercisename, repetitions, weight, rv_position FROM sets;")
            database.execSQL("DROP TABLE sets;")
            database.execSQL("ALTER TABLE sets_backup RENAME TO sets;")
            database.execSQL("PRAGMA foreign_keys=on;")
        }
    }
}