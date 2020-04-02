package pl.lejdi.gymdiary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.lejdi.gymdiary.database.dao.ExerciseDAO
import pl.lejdi.gymdiary.database.dao.SetDAO
import pl.lejdi.gymdiary.database.dao.TrainingDAO
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.database.model.Set
import pl.lejdi.gymdiary.database.model.Training

@Database(entities = [
    Exercise::class,
    Set::class,
    Training::class],
    version = 2)
abstract class GymDatabase : RoomDatabase(){
    companion object{
        val DB_NAME = "gym_db"
        private var instance : GymDatabase? = null

        fun getInstance(context: Context) : GymDatabase?
        {
            if(instance == null)
            {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    GymDatabase::class.java,
                    DB_NAME
                ).build()
            }
            return instance
        }
    }

    abstract fun getExerciseDAO() : ExerciseDAO
    abstract fun getSetDAO() : SetDAO
    abstract fun getTrainingDAO() : TrainingDAO
}