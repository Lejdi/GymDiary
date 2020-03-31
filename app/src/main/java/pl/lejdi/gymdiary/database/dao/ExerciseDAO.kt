package pl.lejdi.gymdiary.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pl.lejdi.gymdiary.database.model.Exercise

interface ExerciseDAO {
    @Insert
    fun insertExercise(vararg exercise : Exercise)

    @Delete
    fun deleteExercise(id: Int)

    @Update
    fun updateExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises")
    fun getAllExercises() : LiveData<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id = :id")
    fun getExercise(id : Int) : LiveData<Exercise>
}