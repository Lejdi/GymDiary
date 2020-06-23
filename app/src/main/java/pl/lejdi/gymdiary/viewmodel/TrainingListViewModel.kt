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

class TrainingListViewModel : MainViewModel() {

    val trainings = MutableLiveData<List<Training>>()

    val dateIsEmpty = MutableLiveData<Boolean>(false)
    val descriptionIsEmpty = MutableLiveData<Boolean>(false)

    fun getCurrentDate() : LiveData<String> {
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

        val newTraining = Training(0, date, description)
        val tmpMutableList = trainings.value?.toMutableList()
        tmpMutableList?.add(newTraining)
        trainings.value = tmpMutableList?.toList()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.insertTraining(newTraining)
            }
        }
        return true
    }

    fun retrieveTrainings(){
        trainings.value = mutableListOf()
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO)
            {
                repo.getAllTrainings()
            }
            trainings.value = response
        }
    }

    fun deleteTraining(training: Training)
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                repo.deleteTraining(training)
            }
        }
        retrieveTrainings()
    }
}