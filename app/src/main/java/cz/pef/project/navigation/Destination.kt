package cz.pef.project.navigation

sealed class Destination(
    val route: String
) {
    object RegistrationScreen : Destination(route = "registration")
    object LoginScreen : Destination(route = "login")

    object GardenOverviewScreen : Destination(route = "garden_overview")
    object UserSettingsScreen : Destination(route = "user_settings")

    object FlowerAiScreen : Destination(route = "plant/{id}/ai")
    object FlowerDescriptionScreen : Destination(route = "plant/{id}/description")
    object FlowerLocationScreen : Destination(route = "plant/{id}/location")
    object FlowerPicturesScreen : Destination(route = "plant/{id}/pictures")

}
