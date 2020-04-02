package pl.lejdi.gymdiary.database

import androidx.room.Database
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
    version = 3)
abstract class GymDatabase : RoomDatabase(){
    companion object{
        val DB_NAME = "gym_db"
    }

    abstract fun getExerciseDAO() : ExerciseDAO
    abstract fun getSetDAO() : SetDAO
    abstract fun getTrainingDAO() : TrainingDAO
}