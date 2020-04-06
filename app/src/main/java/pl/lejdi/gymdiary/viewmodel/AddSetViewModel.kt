package pl.lejdi.gymdiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.database.model.Set

class AddSetViewModel : MainViewModel() {
    val exercises = MutableLiveData<List<Exercise>>()
    val description = MutableLiveData<String>()
    val suggestedWeight = MutableLiveData<Float>()
    val suggestedReps = MutableLiveData<Int>()

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

    fun getExercisesNames() : List<String>
    {
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

    fun calculateSuggestedWeight(type : String) {
        suggestedWeight.value = when(type) {
            "Strength" -> {
                1.0f
            }
            "Hypertrophy" -> {
                2.0f
            }
            "Endurance" -> {
                3.0f
            }
            else -> {
                0f
            }
        }
    }

    fun suggestedReps(type : String) {
        suggestedReps.value = when(type) {
            "Strength" -> {
                1
            }
            "Hypertrophy" -> {
                2
            }
            "Endurance" -> {
                3
            }
            else -> {
                0
            }
        }
    }

    fun saveSet(trainingID : Int, exerciseName : String, weight : String, reps : String, type : String) : Boolean
    {
        if(exerciseName.isEmpty() || weight.isEmpty() || reps.isEmpty())
            return false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val typeInt = when(type) {
                    "Strength" -> 1
                    "Hypertrophy" -> 2
                    "Endurance" -> 3
                    else -> 0
                }
                repo.insertSet(Set(0, trainingID, exerciseName, reps.toInt(), weight.toFloat(), typeInt))
            }
        }
        return true
    }
}