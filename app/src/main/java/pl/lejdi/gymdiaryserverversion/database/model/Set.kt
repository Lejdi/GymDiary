package pl.lejdi.gymdiaryserverversion.database.model

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
    @PrimaryKey(autoGenerate = true) var id : Int,
    @ColumnInfo(name = "trainingid") var trainingID : Int,
    @ColumnInfo(name = "exercisename") var exerciseName : String,
    @ColumnInfo(name = "repetitions") var repetitions : Int,
    @ColumnInfo(name = "weight") var weight : Float,
    @ColumnInfo(name = "updated")var updated : Long
) : Parcelable