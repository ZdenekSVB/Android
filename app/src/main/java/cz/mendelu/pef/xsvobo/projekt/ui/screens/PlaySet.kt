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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaySetScreen(navigationRouter: INavigationRouter) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navigationRouter.returnBack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }

                },
                title = {
                    Text(text = "Play Set")
                })
        },
    ) {

        PlaySetScreenContent(
            paddingValues = it,
            navigationRouter = navigationRouter
        )


    }


}

@Composable
fun PlaySetScreenContent(
    paddingValues: PaddingValues,
    navigationRouter: INavigationRouter
) {

    Column(
        modifier = Modifier.padding(paddingValues)
    ) {
        Button(onClick = {
            navigationRouter.navigateToResults(null)
        }) {
            Text(text = "Next")
        }
    }
}

