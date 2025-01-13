package cz.pef.project.ui.screens.loading_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.screens.garden_overview.GardenOverviewViewModel
import cz.pef.project.ui.theme.basicMargin

@Composable
fun LoadingScreenMap(
    id:Int,
    navigation:INavigationRouter
){
    val viewModel = hiltViewModel<LoadingScreenViewModel>()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState() // Sledujeme nastavení tmavého režimu


    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
        ) {
            padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                navigation.navigateToFlowerLocationScreen(id)
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = basicMargin(), bottom = basicMargin())
                )
            }
        }}
}