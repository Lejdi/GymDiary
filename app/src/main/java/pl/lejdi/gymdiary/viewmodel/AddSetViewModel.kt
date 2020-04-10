package pl.lejdi.gymdiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.database.model.Set
import kotlin.math.round

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

    fun calculateSuggestedWeight(type : String, exerciseName: String) {
        var RM = 0.0f
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO)
            {
                repo.getExerciseByName(exerciseName)
            }
            try{
                if(response.isRMAuto == 0)
                {
                    RM = response.RM
                }
                else
                {
                    val sets = withContext(Dispatchers.IO)
                    {
                        repo.getSetsByExerciseName(exerciseName)
                    }
                    RM = 0.0f
                    sets.forEach {
                        val tmp = calculateRM(it)
                        if (tmp > RM)
                            RM = tmp
                    }
                }
                suggestedWeight.value = when(type) {
                    "Strength" -> {
                        round(0.85f*RM)
                    }
                    "Hypertrophy" -> {
                        round(0.7f*RM)
                    }
                    "Endurance" -> {
                        round(0.35f*RM)
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

    private fun calculateRM(set: Set) : Float
    {
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