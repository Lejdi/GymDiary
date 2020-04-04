package pl.lejdi.gymdiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Training

class TrainingListViewModel : MainViewModel() {

    val trainings = MutableLiveData<List<Training>>()

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
}