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

    val dateIsEmpty = MutableLiveData<Boolean>(false)
    val descriptionIsEmpty = MutableLiveData<Boolean>(false)

    fun getCurrentDate() : LiveData<String>{
        val currentDate = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date())
        val result = MutableLiveData<String>()
        result.value = currentDate
        return result
    }

    fun saveNewTraining(date : String, description : String) : Boolean
    {
        dateIsEmpty.value = date.isEmpty()
        descriptionIsEmpty.value = description.isEmpty()
        if(dateIsEmpty.value!! || descriptionIsEmpty.value!!){
            return false
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.insertTraining(Training(0, date, description))
            }
        }
        return true
    }
}