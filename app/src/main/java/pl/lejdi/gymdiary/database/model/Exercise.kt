package pl.lejdi.gymdiary.database.model

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "exercises")
class Exercise(@ColumnInfo(name = "name") @PrimaryKey(autoGenerate = false) val name : String,
               @ColumnInfo(name = "description")val description : String,
               @ColumnInfo(name = "rm")val RM : Float,
               @ColumnInfo(name = "isrmauto")val isRMAuto : Int)
    : Parcelable