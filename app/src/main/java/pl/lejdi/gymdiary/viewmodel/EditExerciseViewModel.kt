package pl.lejdi.gymdiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Exercise


class EditExerciseViewModel : MainViewModel() {

    val exercise = MutableLiveData<Exercise>()
    val nameIsEmpty = MutableLiveData<Boolean>(false)
    val descriptionIsEmpty = MutableLiveData<Boolean>(false)
    val RMisEmpty = MutableLiveData<Boolean>(false)

    fun saveExercise(name : String, description : String, isRMauto : Boolean, RM : String) : Boolean {
        //check if all fields are filled
        nameIsEmpty.value = name.isEmpty()
        descriptionIsEmpty.value = description.isEmpty()
        RMisEmpty.value = RM.isEmpty()
        if(name.isEmpty() || description.isEmpty() || RM.isEmpty())
            return false

        //save exercise
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var isAuto=0
                if(isRMauto)
                    isAuto=1
                repo.insertExercise(Exercise(name, description, RM.toFloat(), isAuto))
            }
        }
        return true
    }

    fun updateExercise(name : String, description : String, isRMauto : Boolean, RM : String) : Boolean {
        //check if fields are filled
        if(description.isEmpty() || RM.isEmpty())
            return false
        //update exercise
        viewModelScope.launch {
           withContext(Dispatchers.IO) {
                var isAuto=0
                if(isRMauto)
                    isAuto=1
                repo.updateExercise(Exercise(name, description, RM.toFloat(), isAuto))
            }
        }
        return true
    }

    fun retrieveExercise(name : String) {
        //get exercise from DB
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repo.getExerciseByName(name)
            }
            exercise.value=response
        }
    }

}