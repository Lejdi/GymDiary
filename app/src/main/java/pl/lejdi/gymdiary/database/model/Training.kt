package pl.lejdi.gymdiary.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "trainings")
data class Training(
    @ColumnInfo(name = "id")  @PrimaryKey(autoGenerate = true) var id : Int,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "description")var description: String,
    @ColumnInfo(name = "updated")var updated : Long
) : Parcelable