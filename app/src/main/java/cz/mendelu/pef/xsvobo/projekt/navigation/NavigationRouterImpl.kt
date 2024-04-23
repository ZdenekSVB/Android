package cz.mendelu.pef.xsvobo.projekt.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {


    override fun navigateToSetList(id: Long?) {
        navController.navigate(Destination.SetList.route)
    }

    override fun navigateToCodeSet(id: Long?) {
        navController.navigate(Destination.CodeSet.route)
    }

    override fun navigateToCardList(id: Long?) {
        navController.navigate(Destination.CardList.route)
    }

    override fun navigateToAddCard(id: Long?) {
        navController.navigate(Destination.AddCard.route)
    }

    override fun navigateToPlaySet(id: Long?) {
        navController.navigate(Destination.PlaySet.route)
    }

    override fun navigateToResults(id: Long?) {
        navController.navigate(Destination.Results.route)
    }

    override fun getNavController(): NavController {
        return navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }

}