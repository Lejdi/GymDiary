package pl.lejdi.gymdiaryserverversion.database.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "exercises")
data class Exercise(
    @ColumnInfo(name = "name") @PrimaryKey(autoGenerate = false) val name : String,
    @ColumnInfo(name = "description")val description : String,
    @ColumnInfo(name = "rm")val RM : Float,
    @ColumnInfo(name = "isrmauto")val isRMAuto : Int,
    @ColumnInfo(name = "updated")val updated : Long
) : Parcelable