package pl.lejdi.gymdiaryserverversion.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.lejdi.gymdiaryserverversion.database.dao.ExerciseDAO
import pl.lejdi.gymdiaryserverversion.database.dao.SetDAO
import pl.lejdi.gymdiaryserverversion.database.dao.TrainingDAO
import pl.lejdi.gymdiaryserverversion.database.model.Exercise
import pl.lejdi.gymdiaryserverversion.database.model.Set
import pl.lejdi.gymdiaryserverversion.database.model.Training

@Database(entities = [
    Exercise::class,
    Set::class,
    Training::class],
    version = 8)
abstract class GymDatabase : RoomDatabase(){
    companion object{
        val DB_NAME = "gym_db"
    }

    abstract fun getExerciseDAO() : ExerciseDAO
    abstract fun getSetDAO() : SetDAO
    abstract fun getTrainingDAO() : TrainingDAO
}