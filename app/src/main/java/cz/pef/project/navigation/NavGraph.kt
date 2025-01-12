package cz.pef.project.navigation

import GardenOverviewScreen
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.pef.project.communication.Plant
import cz.pef.project.ui.elements.LoadingScreenMap
import cz.pef.project.ui.screens.flower_ai.FlowerAiScreen
import cz.pef.project.ui.screens.flower_description.FlowerDescriptionScreen
import cz.pef.project.ui.screens.flower_location.FlowerLocationScreen
import cz.pef.project.ui.screens.flower_pictures.FlowerPicturesScreen
import cz.pef.project.ui.screens.login.LoginScreen
import cz.pef.project.ui.screens.registration.RegistrationScreen
import cz.pef.project.ui.screens.user_settings.UserSettingsScreen

@ExperimentalFoundationApi
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember { NavigationRouterImpl(navController) },
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Destination.RegistrationScreen.route) {
            RegistrationScreen(navigation = navigation)
        }

        composable(Destination.LoginScreen.route) {
            LoginScreen(navigation = navigation)
        }

        composable(Destination.GardenOverviewScreen.route) {
            GardenOverviewScreen(navigation = navigation)
        }

        composable(Destination.FlowerPicturesScreen.route, arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) {
            val id = it.arguments?.getInt("id")
            FlowerPicturesScreen(navigation = navigation, id = id ?: -1)
        }

        composable(Destination.FlowerDescriptionScreen.route, arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) {
            val id = it.arguments?.getInt("id")
            FlowerDescriptionScreen(navigation = navigation, id = id ?: -1)
        }

        composable(Destination.FlowerAiScreen.route, arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) {
            val id = it.arguments?.getInt("id")
            FlowerAiScreen(navigation = navigation, id = id ?: -1)
        }

        composable(Destination.FlowerLocationScreen.route, arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) {
            val id = it.arguments?.getInt("id")
            FlowerLocationScreen(navigation = navigation, id = id ?: -1)
        }

        composable(Destination.LoadingScreenMap.route, arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) {
            val id = it.arguments?.getInt("id")
            LoadingScreenMap(navigation = navigation, id = id ?: -1)
        }


        composable(Destination.UserSettingsScreen.route) {
            UserSettingsScreen(navigation = navigation)
        }
    }
}
