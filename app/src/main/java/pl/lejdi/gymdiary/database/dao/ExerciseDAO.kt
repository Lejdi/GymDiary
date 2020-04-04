package pl.lejdi.gymdiary.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.lejdi.gymdiary.database.model.Exercise

@Dao
interface ExerciseDAO {
    @Insert
    fun insertExercise(vararg exercise : Exercise)

    @Delete
    fun deleteExercise(exercise : Exercise)

    @Update
    fun updateExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises")
    fun getAllExercises() : List<Exercise>

    @Query("SELECT * FROM exercises WHERE name = :name")
    fun getExercise(name: String) : Exercise
}