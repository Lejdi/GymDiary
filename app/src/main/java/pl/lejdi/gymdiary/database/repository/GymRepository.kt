package pl.lejdi.gymdiary.database.repository

import android.util.Log
import androidx.lifecycle.LiveData
import pl.lejdi.gymdiary.GymDiaryApplication
import pl.lejdi.gymdiary.database.GymDatabase
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.database.model.Set
import pl.lejdi.gymdiary.database.model.Training
import pl.lejdi.gymdiary.dependency.RepositoryModule
import javax.inject.Inject

class GymRepository{

    @Inject
    lateinit var database: GymDatabase

    init{
        GymDiaryApplication.component.repositoryComponent(RepositoryModule()).inject(this)
    }

    fun getAllExercises() : List<Exercise>
    {
        return database.getExerciseDAO().getAllExercises()
    }

    fun getExerciseByName(name : String) : Exercise
    {
        return database.getExerciseDAO().getExercise(name)
    }

    fun getAllTrainings() : List<Training>
    {
        return database.getTrainingDAO().getAllTrainings()
    }

    fun getTrainingByID(id : Int) : Training
    {
        return database.getTrainingDAO().getTraining(id)
    }

    fun getAllSets() : List<Set>
    {
        return database.getSetDAO().getAllSets()
    }

    fun getSetByID(id : Int) : Set
    {
        return database.getSetDAO().getSet(id)
    }

    fun deleteExercise(exercise: Exercise)
    {
        database.getExerciseDAO().deleteExercise(exercise)
    }

    fun deleteTraining(training : Training)
    {
        database.getTrainingDAO().deleteTraining(training)
    }

    fun deleteSet(set: Set)
    {
        database.getSetDAO().deleteSet(set)
    }

    fun insertExercise(exercise: Exercise)
    {
        database.getExerciseDAO().insertExercise(exercise)
    }

    fun insertTraining(training: Training)
    {
        database.getTrainingDAO().insertTraining(training)
    }

    fun insertSet(set: Set)
    {
        database.getSetDAO().insertSet(set)
    }

    fun updateExercise(exercise: Exercise)
    {
        database.getExerciseDAO().updateExercise(exercise)
    }

    fun updateTraining(training: Training)
    {
        database.getTrainingDAO().updateTraining(training)
    }

    fun updateSet(set: Set)
    {
        database.getSetDAO().updateSet(set)
    }
}