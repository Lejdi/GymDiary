package pl.lejdi.gymdiary.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.R
import pl.lejdi.gymdiary.database.model.Exercise
import pl.lejdi.gymdiary.database.model.Set
import kotlin.math.floor
import kotlin.math.round

class AddSetViewModel : MainViewModel() {
    val exercises = MutableLiveData<List<Exercise>>()
    val description = MutableLiveData<String>()
    val suggestedWeight = MutableLiveData<Float>()
    val suggestedReps = MutableLiveData<Int>()
    lateinit var context : Context
    private lateinit var types : Array<String>

    //objects used to display proper warnings
    val exerciseNameIsEmpty = MutableLiveData<Boolean>(false)
    val weightIsEmpty = MutableLiveData<Boolean>(false)
    val repsIsEmpty = MutableLiveData<Boolean>(false)

    //retrieve types from resources
    fun init(context: Context){
        this.context = context
        types = context.resources.getStringArray(R.array.types)
    }

    //get all exercises to populate dropdown list
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

    //extract names from Exercises objects
    fun getExercisesNames() : List<String> {
        val names = mutableListOf<String>()
        exercises.value?.forEach { exerciseObject ->
            names.add(exerciseObject.name)
        }
        return names
    }

    //get description of chosen exercise
    fun getExerciseDescription(name : String){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO)
            {
                repo.getExerciseByName(name)
            }
            description.value = response.description
        }
    }

    //suggesting weights
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
                    types[0] -> {
                        roundWeight(0.85f*RM)
                    }
                    types[1] -> {
                        roundWeight(0.7f*RM)
                    }
                    types[2] -> {
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

    //rounding weight to 0.5
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

    //update exercise's RM when saving new set
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

    private fun calculateRM(weight: Float, reps: Int) : Float {
        return weight * (1+ (reps/30.0f))
    }

    //suggesting repetitions
    fun suggestedReps(type : String) {
        suggestedReps.value = when(type) {
            types[0] -> {
                5
            }
            types[1] -> {
                12
            }
            types[2] -> {
                18
            }
            else -> {
                0
            }
        }
    }

    //saving a set
    fun saveSet(trainingID : Int, exerciseName : String, weight : String, reps : String) : Boolean {
        //check if all data fields are filled
        exerciseNameIsEmpty.value = exerciseName.isEmpty()
        weightIsEmpty.value = weight.isEmpty()
        repsIsEmpty.value = reps.isEmpty()
        if(exerciseName.isEmpty() || weight.isEmpty() || reps.isEmpty())
            return false

        //updating RM
        val newSetRM = calculateRM(weight.toFloat(), reps.toInt())
        updateRM(newSetRM, exerciseName)

        //actual saving
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val setsCount = repo.getSetsByTrainingCount(trainingID)
                repo.insertSet(Set(0, trainingID, exerciseName, reps.toInt(), weight.toFloat(), setsCount))
            }
        }
        return true
    }
}