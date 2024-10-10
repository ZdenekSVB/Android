package cz.pef.va2_2024_petstore.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun navigateToListOfPetsScreen()
    fun navigateToPetDetailScreen(id: Long)
    fun returnBack()
}