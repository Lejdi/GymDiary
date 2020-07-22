package pl.lejdi.gymdiaryserverversion.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "trainings")
data class Training(@ColumnInfo(name = "id")  @PrimaryKey(autoGenerate = true) val id : Int,
               @ColumnInfo(name = "date") val date: String,
               @ColumnInfo(name = "description")val description: String)
    : Parcelable