package cz.pef.project.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.theme.basicMargin

@Composable
fun LoadingScreenMap(
    id:Int,
    navigation:INavigationRouter
){
    MaterialTheme(colorScheme = darkColorScheme()) {
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