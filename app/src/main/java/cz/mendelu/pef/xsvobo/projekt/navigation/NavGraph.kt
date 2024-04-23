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

        composable(Destination.SetList.route){
            SetListScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.CodeSet.route){
            CodeSetScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.CardList.route){
            CardListScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.AddCard.route){
            AddCardScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.PlaySet.route){
            PlaySetScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.Results.route){
            ResultsScreen(navigationRouter = navigationRouter)
        }


    }


}