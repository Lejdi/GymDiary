package pl.lejdi.gymdiary.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = Training::class,
            parentColumns = ["id"],
            childColumns = ["trainingid"],
            onDelete = ForeignKey.CASCADE
        ),
    ForeignKey(
        entity = Exercise::class,
        parentColumns = ["name"],
        childColumns = ["exercisename"],
        onDelete = ForeignKey.CASCADE
    )
    ])
data class Set(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "trainingid") val trainingID : Int,
    @ColumnInfo(name = "exercisename") val exerciseName : String,
    @ColumnInfo(name = "repetitions") val repetitions : Int,
    @ColumnInfo(name = "weight") val weight : Float,
    @ColumnInfo(name = "type") val type : Int,
    @ColumnInfo(name = "rv_position") val rvPosition : Int
) : Parcelable