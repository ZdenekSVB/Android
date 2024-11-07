package cz.pef.mendelu.examtemplate2024.navigation

sealed class Destination(
    val route: String
){
    object MainScreen : Destination(route = "main")
}
