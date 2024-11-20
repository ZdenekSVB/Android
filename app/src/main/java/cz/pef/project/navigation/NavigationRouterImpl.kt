package cz.pef.project.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun getNavController(): NavController = navController

    override fun navigateToGardenOverviewScreen() {
        navigateIfResumed(Destination.GardenOverviewScreen.route)
    }

    override fun navigateToFlowerAiScreen() {
        navigateIfResumed(Destination.FlowerAiScreen.route)
    }

    override fun navigateToFlowerDescriptionScreen() {
        navigateIfResumed(Destination.FlowerDescriptionScreen.route)
    }

    override fun navigateToFlowerMapScreen() {
        navigateIfResumed(Destination.FlowerMapScreen.route)
    }

    override fun navigateToFlowerPicturesScreen() {
        navigateIfResumed(Destination.FlowerPicturesScreen.route)
    }

    override fun navigateToUserSettingsScreen() {
        navigateIfResumed(Destination.UserSettingsScreen.route)
    }

    override fun returnBack() {
        navController.popBackStack()
    }

    private fun navigateIfResumed(route: String) {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(route)
        }
    }
}
