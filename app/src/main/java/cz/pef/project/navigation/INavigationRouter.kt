package cz.pef.project.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun navigateToGardenOverviewScreen()
    fun navigateToFlowerAiScreen()
    fun navigateToFlowerDescriptionScreen()
    fun navigateToFlowerMapScreen()
    fun navigateToFlowerPicturesScreen()
    fun navigateToUserSettingsScreen()
    fun returnBack()
}
