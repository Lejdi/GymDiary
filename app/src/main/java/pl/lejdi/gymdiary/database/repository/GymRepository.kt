package pl.lejdi.gymdiary.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import pl.lejdi.gymdiary.database.GymDatabase
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.database.model.Set
import pl.lejdi.gymdiary.database.model.Training

class GymRepository internal constructor(context: Context){
    private var database : GymDatabase? = GymDatabase.getInstance(context)

    fun getAllExercises() : LiveData<List<Exercise>>?
    {
        return database?.getExerciseDAO()?.getAllExercises()
    }

    fun getExerciseByID(id : Int) : LiveData<Exercise>?
    {
        return database?.getExerciseDAO()?.getExercise(id)
    }

    fun getAllTrainings() : LiveData<List<Training>>?
    {
        return database?.getTrainingDAO()?.getAllTrainings()
    }

    fun getTrainingByID(id : Int) : LiveData<Training>?
    {
        return database?.getTrainingDAO()?.getTraining(id)
    }

    fun getAllSets() : LiveData<List<Set>>?
    {
        return database?.getSetDAO()?.getAllSets()
    }

    fun getSetByID(id : Int) : LiveData<Set>?
    {
        return database?.getSetDAO()?.getSet(id)
    }

    fun deleteExercise(exercise: Exercise)
    {
        database?.getExerciseDAO()?.deleteExercise(exercise)
    }

    fun deleteTraining(training : Training)
    {
        database?.getTrainingDAO()?.deleteTraining(training)
    }

    fun deleteSet(set: Set)
    {
        database?.getSetDAO()?.deleteSet(set)
    }

    fun insertExercise(exercise: Exercise)
    {
        database?.getExerciseDAO()?.insertExercise(exercise)
    }

    fun insertTraining(training: Training)
    {
        database?.getTrainingDAO()?.insertTraining(training)
    }

    fun insertSet(set: Set)
    {
        database?.getSetDAO()?.insertSet(set)
    }

    fun updateExercise(exercise: Exercise)
    {
        database?.getExerciseDAO()?.updateExercise(exercise)
    }

    fun updateTraining(training: Training)
    {
        database?.getTrainingDAO()?.updateTraining(training)
    }

    fun updateSet(set: Set)
    {
        database?.getSetDAO()?.updateSet(set)
    }
}