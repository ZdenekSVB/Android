package cz.mendelu.pef.xsvobo.projekt.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun navigateToMenuScreen(id: Long?) {
        navController.navigate(Destination.MenuScreen.route)
    }
    override fun navigateToSetListScreen(id: Long?) {
        navController.navigate(Destination.SetListScreen.route)
    }

    override fun navigateToCodeSetScreen(id: Long?) {
        navController.navigate(Destination.CodeSetScreen.route)
    }

    override fun navigateToCardListScreen(id: Long) {
        navController.navigate(Destination.CardListScreen.route + "/" + id)
    }

    override fun navigateToAddCardScreen(id: Long) {
            navController.navigate(Destination.AddCardScreen.route + "/" + id)
    }

    override fun navigateToPlaySetScreen(id: Long?) {
        navController.navigate(Destination.PlaySetScreen.route + "/" + id)
    }

    override fun navigateToResultsScreen(id: Long?) {
        navController.navigate(Destination.ResultsScreen.route)
    }

    override fun getNavController(): NavController {
        return navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }

}