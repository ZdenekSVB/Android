package cz.pef.project.communication

data class Plant(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val isHealthy: Boolean,
    val lastCondition: String = "Unknown" // Přidáno pro poslední stav
)
