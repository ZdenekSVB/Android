package cz.pef.project.communication

data class Plant(
    val id: Int,
    val name: String,
    var lastCondition: String = "Unknown" // Přidáno pro poslední stav
)
