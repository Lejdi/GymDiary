package pl.lejdi.gymdiaryserverversion.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.lejdi.gymdiaryserverversion.adapter.SetListAdapter
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
    fun notifyOrderChanged(viewholder1 : SetListAdapter.ViewHolder, viewholder2 : SetListAdapter.ViewHolder){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val updateTime = System.currentTimeMillis()
                repo.updateSet(Set(viewholder1.mItem!!.id, viewholder2.mItem!!.trainingID, viewholder2.mItem!!.exerciseName, viewholder2.mItem!!.repetitions, viewholder2.mItem!!.weight, updateTime))
                repo.updateSet(Set(viewholder2.mItem!!.id, viewholder1.mItem!!.trainingID, viewholder1.mItem!!.exerciseName, viewholder1.mItem!!.repetitions, viewholder1.mItem!!.weight, updateTime))
                val tmp = viewholder1.mItem!!.id
                viewholder1.mItem!!.id = viewholder2.mItem!!.id
                viewholder2.mItem!!.id = tmp
            }
        }
    }
}