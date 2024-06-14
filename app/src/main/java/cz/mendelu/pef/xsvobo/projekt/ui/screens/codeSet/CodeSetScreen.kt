package cz.mendelu.pef.xsvobo.projekt.ui.screens.codeSet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import cz.mendelu.pef.xsvobo.projekt.R
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeSetScreen(navigationRouter: INavigationRouter) {
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
                    Text(text = stringResource(id = R.string.code_set_title))
                })
        },
    ) {

        CodeSetScreenContent(
            paddingValues = it,
            navigationRouter=navigationRouter
        )


    }


}

@Composable
fun CodeSetScreenContent(
    paddingValues: PaddingValues,
    navigationRouter: INavigationRouter
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Set the background color to gray
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        TextField(value = "", onValueChange = {})//TODO
        Button(onClick = {
            navigationRouter.returnBack()
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White)) {
            Text(text = "Add Set")
        }
    }


}