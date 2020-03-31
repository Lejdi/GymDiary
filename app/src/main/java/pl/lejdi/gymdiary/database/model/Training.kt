package pl.lejdi.gymdiary.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "trainings")
class Training(@PrimaryKey(autoGenerate = true) val id : Int,
               @ColumnInfo(name = "date") val date: String,
               @ColumnInfo(name = "description")val description: String)
    : Parcelable