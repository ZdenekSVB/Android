package cz.pef.project.navigation

sealed class Destination(
    val route: String
) {
    object RegistrationScreen : Destination(route = "registration")
    object LoginScreen : Destination(route = "login")
    object GardenOverviewScreen : Destination(route = "garden_overview")
    object FlowerAiScreen : Destination(route = "flower_ai")
    object FlowerDescriptionScreen : Destination(route = "flower_description")
    object FlowerMapScreen : Destination(route = "flower_map")
    object FlowerPicturesScreen : Destination(route = "flower_pictures")
    object UserSettingsScreen : Destination(route = "user_settings")
}
