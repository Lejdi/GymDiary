package pl.lejdi.gymdiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Exercise

class ExerciseListViewModel : MainViewModel() {
    val exercises = MutableLiveData<List<Exercise>>()

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

    fun deleteExercise(exercise: Exercise)
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                repo.deleteExercise(exercise)
            }
        }
        retrieveExercises()
    }
}