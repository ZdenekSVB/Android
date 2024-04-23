package cz.mendelu.pef.xsvobo.projekt.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun navigateToSetList(id: Long?)
    fun navigateToCodeSet(id: Long?)
    fun navigateToCardList(id: Long?)
    fun navigateToAddCard(id: Long?)
    fun navigateToPlaySet(id: Long?)
    fun navigateToResults(id: Long?)
    fun getNavController(): NavController
    fun returnBack()
}