package cz.pef.project.communication

data class GardenCenterResponse(
    val type: String,
    val features: List<Feature>
)

data class Feature(
    val type: String,
    val properties: Properties,
    val geometry: Geometry
)

data class Properties(
    val name: String,
    val address: String,
    val description: String
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>
)
