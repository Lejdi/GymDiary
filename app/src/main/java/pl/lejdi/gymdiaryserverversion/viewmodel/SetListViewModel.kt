package pl.lejdi.gymdiaryserverversion.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiaryserverversion.database.model.Set

class SetListViewModel : MainViewModel() {

    val sets = MutableLiveData<MutableList<Set>>()

    //get all sets of specific training
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

    //delete set
    fun deleteSet(set: Set) {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                repo.deleteSet(set)
            }
        }
        retrieveSets(set.trainingID)
    }

    //handling reorganizing items - every change update very set on the list
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