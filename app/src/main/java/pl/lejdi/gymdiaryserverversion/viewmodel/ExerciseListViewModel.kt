package pl.lejdi.gymdiaryserverversion.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiaryserverversion.database.model.Exercise

class ExerciseListViewModel : MainViewModel() {
    val exercises = MutableLiveData<List<Exercise>>()

    //get all exercises
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

    //remove exercise
    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                repo.deleteExercise(exercise)
            }
        }
        retrieveExercises()
    }
}