package cz.pef.project.navigation

import androidx.navigation.NavController
import cz.pef.project.communication.Plant

interface INavigationRouter {
    fun getNavController(): NavController

    fun navigateToRegistration()
    fun navigateToLogin()

    fun navigateToGardenOverviewScreen()
    fun navigateToUserSettingsScreen()

    fun navigateToFlowerAiScreen(id: Int)
    fun navigateToFlowerDescriptionScreen(id: Int)
    fun navigateToFlowerLocationScreen(id: Int)
    fun navigateToFlowerPicturesScreen(id: Int)

    fun returnBack()
}
