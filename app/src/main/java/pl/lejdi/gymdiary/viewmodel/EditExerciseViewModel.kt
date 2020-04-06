package pl.lejdi.gymdiary.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Exercise


class EditExerciseViewModel : MainViewModel() {
    fun saveExercise(name : String, description : String, isRMauto : Boolean, RM : String) : Boolean
    {
        if(name.isEmpty() || description.isEmpty() || RM.isEmpty())
            return false
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

    fun updateExercise(name : String, description : String, isRMauto : Boolean, RM : String) : Boolean
    {
        if(description.isEmpty() || RM.isEmpty())
            return false
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

}