package pl.lejdi.gymdiaryserverversion.database.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "exercises")
data class Exercise(
    @ColumnInfo(name = "name") @PrimaryKey(autoGenerate = false) var name : String,
    @ColumnInfo(name = "description")var description : String,
    @ColumnInfo(name = "rm")var RM : Float,
    @ColumnInfo(name = "isrmauto")var isRMAuto : Int,
    @ColumnInfo(name = "updated")var updated : Long
) : Parcelable