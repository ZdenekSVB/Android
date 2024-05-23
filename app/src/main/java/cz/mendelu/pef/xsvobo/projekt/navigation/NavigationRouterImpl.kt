package cz.mendelu.pef.xsvobo.projekt.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun navigateToMenu(id: Long?) {
        navController.navigate(Destination.MenuScreen.route)
    }
    override fun navigateToSetList(id: Long?) {
        navController.navigate(Destination.SetListScreen.route)
    }

    override fun navigateToCodeSet(id: Long?) {
        navController.navigate(Destination.CodeSetScreen.route)
    }

    override fun navigateToCardList(id: Long?) {
        navController.navigate(Destination.CardListScreen.route)
    }

    override fun navigateToAddCard(id: Long?) {
        navController.navigate(Destination.AddCardScreen.route)
    }

    override fun navigateToPlaySet(id: Long?) {
        navController.navigate(Destination.PlaySetScreen.route)
    }

    override fun navigateToResults(id: Long?) {
        navController.navigate(Destination.ResultsScreen.route)
    }

    override fun getNavController(): NavController {
        return navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }

}