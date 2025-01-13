package cz.pef.project.DB

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import cz.pef.project.communication.Plant

@Entity(
    tableName = "plants",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["userId"])]
)
data class PlantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    var name: String,
    var description: String?,
    var lastCondition: String?,
    var plantedDate: String?,
    var deathDate: String?,
    var latitude: Double = 50.00, // New field for latitude
    var longitude: Double = 14.00 // New field for longitude
)

fun PlantEntity.toPlant(): Plant {
    return Plant(
        id = id,
        name = this.name,
        lastCondition = this.lastCondition ?: "Unknown",
    )
}
