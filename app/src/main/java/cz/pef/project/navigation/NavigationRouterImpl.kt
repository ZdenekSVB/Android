package cz.pef.project.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import cz.pef.project.communication.Plant

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun getNavController(): NavController = navController

    override fun navigateToGardenOverviewScreen() {
        navigateIfResumed(Destination.GardenOverviewScreen.route)
    }

    override fun navigateToRegistration() {
        navigateIfResumed(Destination.RegistrationScreen.route)
    }

    override fun navigateToLogin() {
        navigateIfResumed(Destination.LoginScreen.route)
    }

    override fun navigateToFlowerAiScreen(id: Int) {
        navigateIfResumed(Destination.FlowerAiScreen.route.replace("{id}", id.toString()))
    }

    override fun navigateToFlowerDescriptionScreen(id: Int) {
        navigateIfResumed(Destination.FlowerDescriptionScreen.route.replace("{id}", id.toString()))
    }

    override fun navigateToFlowerLocationScreen(id: Int) {
        navigateIfResumed(Destination.FlowerLocationScreen.route.replace("{id}", id.toString()))
    }

    override fun navigateToFlowerPicturesScreen(id: Int) {
        navigateIfResumed(Destination.FlowerPicturesScreen.route.replace("{id}", id.toString()))
    }

    override fun navigateToLoadingScreenMap(id: Int) {
        navigateIfResumed(Destination.LoadingScreenMap.route.replace("{id}", id.toString()))
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
