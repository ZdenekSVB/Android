package com.example.mystery.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun getNavController(): NavController = navController

    override fun navigateToListOfPetsScreen() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.ListOfPetsScreen.route)
        }
    }

    override fun navigateToPetDetailScreen(id: Long) {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate("${Destination.PetDetailScreen.route}/${id}")
        }
    }

    override fun returnBack() {
        navController.popBackStack()
    }
}