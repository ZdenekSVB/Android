package cz.mendelu.pef.xsvobo.projekt.ui.screens.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navigationRouter: INavigationRouter) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navigationRouter.navigateToMenuScreen(null)
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }

                },
                title = {
                    Text(text = "Results")
                })
        },
    ) {

        ResultsScreenContent(
            paddingValues = it
        )


    }


}

@Composable
fun ResultsScreenContent(
    paddingValues: PaddingValues
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
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(text = "Set Completed ")
            Icon(imageVector = Icons.Default.Check, contentDescription = "")
        }
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
           //TODO CHART
        }
        //TODO počítaní odpovědí
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(text = "Correct Answers: ${0} / ${0}")
        }
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(text = "Wrong Answers: ${0} / ${0}")
        }

    }


}