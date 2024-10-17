package com.example.mystery.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun navigateToListOfPetsScreen()
    fun navigateToPetDetailScreen(id: Long)
    fun returnBack()
}