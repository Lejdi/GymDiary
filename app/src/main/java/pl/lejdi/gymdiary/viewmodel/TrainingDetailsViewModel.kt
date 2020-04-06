package pl.lejdi.gymdiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Set

class TrainingDetailsViewModel : MainViewModel() {

    val sets = MutableLiveData<List<Set>>()

    fun retrieveSets(trainingId : Int){
        sets.value = mutableListOf()
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO)
            {
                repo.getAllSetsByTraining(trainingId)
            }
            sets.value = response
        }
    }
}