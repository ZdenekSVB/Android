package cz.pef.va2_2024_petstore.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.pef.va2_2024_petstore.ui.screens.list_of_pets.ListOfPetsScreen
import cz.pef.va2_2024_petstore.ui.screens.pet_detail.PetDetailScreen

@ExperimentalFoundationApi
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember { NavigationRouterImpl(navController) },
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination){

        composable(Destination.ListOfPetsScreen.route) {
            ListOfPetsScreen(navigation = navigation)
        }

        composable(Destination.PetDetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            PetDetailScreen(navigation = navigation, id = if (id != -1L) id else null)
        }
    }
}
