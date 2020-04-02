package pl.lejdi.gymdiary.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.lejdi.gymdiary.database.model.Set

@Dao
interface SetDAO {
    @Insert
    fun insertSet(vararg set : Set)

    @Delete
    fun deleteSet(set : Set)

    @Update
    fun updateSet(set: Set)

    @Query("SELECT * FROM sets")
    fun getAllSets() : LiveData<List<Set>>

    @Query("SELECT * FROM sets WHERE id = :id")
    fun getSet(id : Int) : LiveData<Set>
}