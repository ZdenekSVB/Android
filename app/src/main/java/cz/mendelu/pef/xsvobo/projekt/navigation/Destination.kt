package cz.mendelu.pef.xsvobo.projekt.navigation

sealed class Destination(val route: String) {
    object MenuScreen : Destination("menu")
    object SetListScreen : Destination("set_list")
    object CodeSetScreen : Destination("code_set")
    object CardListScreen : Destination("card_list")
    object AddCardScreen : Destination("add_card")
    object PlaySetScreen : Destination("play_set")
    object ResultsScreen : Destination("results")
    object AppInfoScreen : Destination("app_Info")
}