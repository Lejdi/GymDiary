package pl.lejdi.gymdiary.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.database.model.Set
import kotlin.math.floor
import kotlin.math.round

class AddSetViewModel : MainViewModel() {
    val exercises = MutableLiveData<List<Exercise>>()
    val description = MutableLiveData<String>()
    val suggestedWeight = MutableLiveData<Float>()
    val suggestedReps = MutableLiveData<Int>()

    val exerciseNameIsEmpty = MutableLiveData<Boolean>(false)
    val weightIsEmpty = MutableLiveData<Boolean>(false)
    val repsIsEmpty = MutableLiveData<Boolean>(false)

    fun retrieveExercises(){
        exercises.value = mutableListOf()
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO)
            {
                repo.getAllExercises()
            }
            exercises.value = response
        }
    }

    fun getExercisesNames() : List<String> {
        val names = mutableListOf<String>()
        exercises.value?.forEach { exerciseObject ->
            names.add(exerciseObject.name)
        }
        return names
    }

    fun getExerciseDescription(name : String){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO)
            {
                repo.getExerciseByName(name)
            }
            description.value = response.description
        }
    }

    fun calculateSuggestedWeight(type : String, exerciseName: String) {
        var RM : Float
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO)
            {
                repo.getExerciseByName(exerciseName)
            }
            try{
                RM = response.RM
                suggestedWeight.value = when(type) {
                    "Strength" -> {
                        roundWeight(0.85f*RM)
                    }
                    "Hypertrophy" -> {
                        roundWeight(0.7f*RM)
                    }
                    "Endurance" -> {
                        roundWeight(0.35f*RM)
                    }
                    else -> {
                        0f
                    }
                }
            }
            catch(e: Exception){
                RM = 0.0f
                description.value=""
            }
        }
    }

    private fun roundWeight(input : Float) : Float{
        val tmp = round(input*10)
        val decimal = if(tmp%10 < 5){
            if(tmp%10 == 0f){
                0.0f
            }
            else{
                0.5f
            }
        } else{
            1.0f
        }
        return floor(input)+decimal
    }

    private fun updateRM(newRM : Float, exerciseName: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                val exercise = repo.getExerciseByName(exerciseName)
                if (exercise.isRMAuto==1 && newRM > exercise.RM) {
                    val updatedExercise =
                        Exercise(exercise.name, exercise.description, newRM, exercise.isRMAuto)
                    repo.updateExercise(updatedExercise)
                }
            }
        }
    }

    private fun calculateRM(set: Set) : Float {
        return set.weight * (1+ (set.repetitions/30.0f))
    }

    fun suggestedReps(type : String) {
        suggestedReps.value = when(type) {
            "Strength" -> {
                5
            }
            "Hypertrophy" -> {
                12
            }
            "Endurance" -> {
                18
            }
            else -> {
                0
            }
        }
    }

    fun saveSet(trainingID : Int, exerciseName : String, weight : String, reps : String, type : String) : Boolean {
        exerciseNameIsEmpty.value = exerciseName.isEmpty()
        weightIsEmpty.value = weight.isEmpty()
        repsIsEmpty.value = reps.isEmpty()
        if(exerciseName.isEmpty() || weight.isEmpty() || reps.isEmpty())
            return false

        val typeInt = when(type) {
            "Strength" -> 1
            "Hypertrophy" -> 2
            "Endurance" -> 3
            else -> 0
        }

        val newSet = Set(0, trainingID, exerciseName, reps.toInt(), weight.toFloat(), typeInt)
        val newSetRM = calculateRM(newSet)
        updateRM(newSetRM, exerciseName)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.insertSet(newSet)
            }
        }
        return true
    }
}