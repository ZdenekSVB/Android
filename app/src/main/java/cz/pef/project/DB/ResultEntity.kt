package cz.pef.project.DB

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "results",
    foreignKeys = [ForeignKey(
        entity = PlantEntity::class,
        parentColumns = ["id"],
        childColumns = ["plantId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plantId: Int,
    val condition: String,
    val description: String
)


