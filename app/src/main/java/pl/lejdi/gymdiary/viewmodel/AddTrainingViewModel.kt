package pl.lejdi.gymdiary.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Training
import java.util.*

class AddTrainingViewModel : MainViewModel() {

    fun getCurrentDate() : LiveData<String>{
        val currentDate = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date())
        val result = MutableLiveData<String>()
        result.value = currentDate
        return result
    }

    fun saveNewTraining(date : String, description : String) : Boolean
    {
        if(date.isEmpty() || description.isEmpty())
            return false
        viewModelScope.launch {
            addTraining(date, description)
        }
        return true
    }

    suspend fun addTraining(date : String, description : String) = withContext(Dispatchers.Default){
        repo.insertTraining(Training(0, date, description))
    }
}