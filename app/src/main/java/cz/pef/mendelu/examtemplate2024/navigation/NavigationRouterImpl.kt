package cz.pef.mendelu.examtemplate2024.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun getNavController(): NavController = navController

    override fun returnBack() {
        navController.popBackStack()
    }
    override fun navigateToMainScreen(id: Long) {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate("user_$id")
        }
    }
}