package pl.lejdi.gymdiary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Exercise

class AddSetViewModel : MainViewModel() {
    val exercises = MutableLiveData<List<Exercise>>()
    val description = MutableLiveData<String>()

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
}