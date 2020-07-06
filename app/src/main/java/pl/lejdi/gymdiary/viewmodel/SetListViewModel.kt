package pl.lejdi.gymdiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiary.database.model.Set

class SetListViewModel : MainViewModel() {

    val sets = MutableLiveData<MutableList<Set>>()

    fun retrieveSets(trainingId : Int){
        sets.value = mutableListOf()
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO)
            {
                repo.getAllSetsByTraining(trainingId)
            }
            sets.value = response.toMutableList()
            sets.value?.sortBy {
                it.rvPosition
            }
        }
    }

    fun deleteSet(set: Set) {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                repo.deleteSet(set)
            }
        }
        retrieveSets(set.trainingID)
    }

    fun notifyOrderChanged(){
        var rvIdx = 0
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                sets.value?.forEach {
                    repo.updateSet(Set(it.id, it.trainingID, it.exerciseName, it.repetitions, it.weight, rvIdx++))
                }
            }
        }
    }
}