package cz.pef.project.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.pef.project.ui.screens.flower_ai.FlowerAiScreen
import cz.pef.project.ui.screens.flower_description.FlowerDescriptionScreen
import cz.pef.project.ui.screens.flower_location.FlowerLocationScreen
import cz.pef.project.ui.screens.flower_pictures.FlowerPicturesScreen
import cz.pef.project.ui.screens.garden_overview.GardenOverviewScreen
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

        composable(Destination.FlowerAiScreen.route) {
            FlowerAiScreen(navigation = navigation)
        }

        composable(Destination.FlowerDescriptionScreen.route) {
           FlowerDescriptionScreen(navigation = navigation)
        }

        composable(Destination.FlowerMapScreen.route) {
            FlowerLocationScreen(navigation = navigation)
        }

        composable(Destination.FlowerPicturesScreen.route) {
            FlowerPicturesScreen(navigation = navigation)
        }

        composable(Destination.UserSettingsScreen.route) {
            UserSettingsScreen(navigation = navigation)
        }
    }
}
