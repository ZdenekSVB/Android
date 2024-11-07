package cz.pef.mendelu.examtemplate2024.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun navigateToMainScreen(id: Long)
    fun returnBack()
}