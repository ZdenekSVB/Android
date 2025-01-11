package cz.pef.project.DB

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import cz.pef.project.communication.Plant

@Entity(
    tableName = "plants",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val name: String,
    val description: String,
    val plantedDate: String,
    val deathDate: String?
)

fun PlantEntity.toPlant(): Plant {
    return Plant(
        id = id,
        name = this.name,
        imageUrl = "",
        isHealthy = true
    )
}
