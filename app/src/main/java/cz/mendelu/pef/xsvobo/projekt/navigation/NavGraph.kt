package cz.mendelu.pef.xsvobo.projekt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.xsvobo.projekt.ui.screens.MenuScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.SetListScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.CardListScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.AddCardScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.CodeSetScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.PlaySetScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.ResultsScreen

@Composable
fun NavGraph(
    navHostController: NavHostController = rememberNavController(),
    navigationRouter: INavigationRouter = remember {
        NavigationRouterImpl(navController = navHostController)
    },
    startDestination: String
){

    NavHost(navController = navHostController, startDestination = startDestination){

        composable(Destination.MenuScreen.route){
            MenuScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.SetListScreen.route){
            SetListScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.CodeSetScreen.route){
            CodeSetScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.CardListScreen.route){
            CardListScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.AddCardScreen.route){
            AddCardScreen(
                navigationRouter = navigationRouter)
        }

        composable(Destination.PlaySetScreen.route){
            PlaySetScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.ResultsScreen.route){
            ResultsScreen(navigationRouter = navigationRouter)
        }


    }


}