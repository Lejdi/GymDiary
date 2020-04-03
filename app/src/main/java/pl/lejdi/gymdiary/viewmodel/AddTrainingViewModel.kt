package pl.lejdi.gymdiary.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        TODO("SAVE TO DB")
        return true
    }
}