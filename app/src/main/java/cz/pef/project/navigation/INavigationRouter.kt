package cz.pef.project.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController

    fun navigateToRegistration()
    fun navigateToLogin()

    fun navigateToGardenOverviewScreen()
    fun navigateToUserSettingsScreen()

    fun navigateToLoadingScreenMap(id: Int)
    fun navigateToFlowerAiScreen(id: Int)
    fun navigateToFlowerDescriptionScreen(id: Int)
    fun navigateToFlowerLocationScreen(id: Int)
    fun navigateToFlowerPicturesScreen(id: Int)

    fun returnBack()
}
