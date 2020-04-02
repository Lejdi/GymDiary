package pl.lejdi.gymdiary.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "sets")
class Set(@ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id : Int,
          @ColumnInfo(name = "trainingid") val trainingID : Int,
          @ColumnInfo(name = "exerciseid") val exerciseID : Int,
          @ColumnInfo(name = "repetitions") val repetitions : Int,
          @ColumnInfo(name = "weight") val weight : Float,
          @ColumnInfo(name = "type") val type : Int)
    : Parcelable