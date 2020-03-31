package pl.lejdi.gymdiary.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "exercises")
class Exercise(@PrimaryKey(autoGenerate = true) val id : Int,
               @ColumnInfo(name = "name")val name : String,
               @ColumnInfo(name = "description")val description : String,
               @ColumnInfo(name = "rm")val RM : Float,
               @ColumnInfo(name = "isrmauto")val isRMAuto : Int)
    : Parcelable