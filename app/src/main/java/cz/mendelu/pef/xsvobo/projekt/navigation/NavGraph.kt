package cz.mendelu.pef.xsvobo.projekt.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.pef.xsvobo.projekt.ui.screens.menu.MenuScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList.CardListScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.addCard.AddCardScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.appInfo.AppInfoScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.playSet.PlaySetScreen
import cz.mendelu.pef.xsvobo.projekt.ui.screens.results.ResultsScreen

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavGraph(
    navHostController: NavHostController = rememberNavController(),
    navigationRouter: INavigationRouter = remember {
        NavigationRouterImpl(navController = navHostController)
    },
    startDestination: String
) {

    NavHost(navController = navHostController, startDestination = startDestination) {

        composable(Destination.MenuScreen.route) {
            MenuScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.SetListScreen.route) {
            SetListScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.AppInfoScreen.route) {
            AppInfoScreen(navigationRouter = navigationRouter)
        }

        composable(Destination.CardListScreen.route + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            })) {
            val id = it.arguments?.getLong("id")
            if (id != null) {
                CardListScreen(
                    navigationRouter = navigationRouter, id = id
                )
            }
        }

        composable(Destination.AddCardScreen.route + "/{id}", arguments = listOf(navArgument("id") {
            type = NavType.LongType
            defaultValue = -1L
        })) {
            val id = it.arguments?.getLong("id")
            AddCardScreen(
                navigationRouter = navigationRouter, id = id
            )
        }

        composable(Destination.PlaySetScreen.route + "/{id}", arguments = listOf(navArgument("id") {
            type = NavType.LongType
            defaultValue = -1L
        })) {
            val id = it.arguments?.getLong("id")
            if (id != null) {
                PlaySetScreen(
                    navigationRouter = navigationRouter, id = id
                )
            }
        }

        composable("${Destination.ResultsScreen}/{id}/{correctCount}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            }, navArgument("correctCount") {
                type = NavType.IntType
                defaultValue = -1
            })) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id")
            val correctCount = backStackEntry.arguments?.getInt("correctCount")
            if (id != null && correctCount != null) {
                ResultsScreen(
                    navigationRouter = navigationRouter, setId = id, correctCount = correctCount
                )
            }
        }


    }


}