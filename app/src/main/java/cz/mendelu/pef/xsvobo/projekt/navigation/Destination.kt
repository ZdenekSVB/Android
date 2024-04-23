package cz.mendelu.pef.xsvobo.projekt.navigation

sealed class Destination(val route: String) {
    object MenuScreen : Destination("menu")
    object SetList : Destination("set_list")
    object CodeSet : Destination("code_set")
    object CardList : Destination("card_list")
    object AddCard : Destination("add_card")
    object PlaySet : Destination("play_set")
    object Results : Destination("results")
}