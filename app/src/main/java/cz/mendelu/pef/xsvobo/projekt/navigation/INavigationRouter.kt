package cz.mendelu.pef.xsvobo.projekt.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun navigateToMenuScreen(id: Long?)
    fun navigateToSetListScreen(id: Long?)
    fun navigateToCodeSetScreen(id: Long?)
    fun navigateToCardListScreen(id: Long)
    fun navigateToAddCardScreen(id: Long)
    fun navigateToPlaySetScreen(id: Long?)
    fun navigateToResultsScreen(id: Long?)
    fun getNavController(): NavController
    fun returnBack()
}