package pl.lejdi.gymdiary.database.repository

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

    fun deleteExercise(exercise: Exercise){
        database.getExerciseDAO().deleteExercise(exercise)
    }

    fun getAllTrainings() : List<Training>
    {
        return database.getTrainingDAO().getAllTrainings()
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

    fun getSetsByExerciseName(name : String) : List<Set>
    {
        return database.getSetDAO().getAllSetsWithExerciseName(name)
    }

    fun getAllSetsByTraining(trainingId : Int) : List<Set>
    {
        return database.getSetDAO().getAllSetsWithTrainingID(trainingId)
    }
}