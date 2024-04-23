package cz.mendelu.pef.xsvobo.projekt.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navigationRouter: INavigationRouter) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Menu")
                })
        },
    ) {

        MenuScreenContent(
            paddingValues = it,
            navigationRouter=navigationRouter
        )


    }


}

@Composable
fun MenuScreenContent(
    paddingValues: PaddingValues,
    navigationRouter: INavigationRouter
) {


    Column(
        modifier = Modifier.padding(paddingValues)
    )
    {
        Button(onClick = {
            navigationRouter.navigateToSetList(null)
        }) {
            Text(text = "Show Sets")
        }
        Button(onClick = {
            navigationRouter.navigateToCodeSet(null)
        }) {
            Text(text = "Add set from code")
        }
    }


}