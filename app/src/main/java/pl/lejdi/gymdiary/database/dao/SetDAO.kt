package pl.lejdi.gymdiary.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pl.lejdi.gymdiary.database.model.Set

interface SetDAO {
    @Insert
    fun insertSet(vararg set : Set)

    @Delete
    fun deleteSet(id: Int)

    @Update
    fun updateSet(set: Set)

    @Query("SELECT * FROM sets")
    fun getAllSets() : LiveData<List<Set>>

    @Query("SELECT * FROM sets WHERE id = :id")
    fun getSet(id : Int) : LiveData<Set>
}