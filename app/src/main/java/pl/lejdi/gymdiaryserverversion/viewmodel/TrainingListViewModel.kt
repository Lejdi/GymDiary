package pl.lejdi.gymdiaryserverversion.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiaryserverversion.database.model.Training
import java.util.*

class TrainingListViewModel : MainViewModel() {

    val trainings = MutableLiveData<List<Training>>()

    val dateIsEmpty = MutableLiveData<Boolean>(false)
    val descriptionIsEmpty = MutableLiveData<Boolean>(false)

    //providing automatic current date when adding training
    fun getCurrentDate() : LiveData<String> {
        val currentDate = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date())
        val result = MutableLiveData<String>()
        result.value = currentDate
        return result
    }

    //saving new training
    fun saveNewTraining(date : String, description : String) : Boolean {
        //check if all fields are filled
        dateIsEmpty.value = date.isEmpty()
        descriptionIsEmpty.value = description.isEmpty()
        if(dateIsEmpty.value!! || descriptionIsEmpty.value!!){
            return false
        }

        //save training
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val updateTime = System.currentTimeMillis()
                repo.insertTraining(Training(0, date, description, updateTime))
            }
        }
        return true
    }

    //get all trainings
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

    //delete specific training
    fun deleteTraining(training: Training) {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                repo.deleteTraining(training)
            }
        }
        retrieveTrainings()
    }
}