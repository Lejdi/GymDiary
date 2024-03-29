package pl.lejdi.gymdiary.database.dao

import androidx.room.*
import pl.lejdi.gymdiary.database.model.Training

@Dao
interface TrainingDAO {
    @Insert
    fun insertTraining(vararg training : Training)

    @Delete
    fun deleteTraining(training : Training)

    @Update
    fun updateTraining(training: Training)

    @Query("SELECT * FROM trainings")
    fun getAllTrainings() : List<Training>

    @Query("SELECT * FROM trainings WHERE id = :id")
    fun getTraining(id : Int) : Training
}