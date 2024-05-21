package cz.mendelu.pef.xsvobo.projekt.ui.screens

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
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(navigationRouter: INavigationRouter) {
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
                    Text(text = "Add Card")
                })
        },
    ) {

        AddCardScreenContent(
            paddingValues = it,
            navigationRouter=navigationRouter
        )


    }


}

@Composable
fun AddCardScreenContent(
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

        TextField(
            label = { Text(text = "Question")},
            value = "TEST",//TODO data.card.question
            onValueChange = {
                //TODO actions.cardTextChanged(it)
            },
            //TODO ERROR

        )
        TextField(
            label = { Text(text = "Answer")},
            value = "TEST",//TODO data.card.answer
            onValueChange = {
                //TODO actions.cardTextChanged(it)
            },
            //TODO ERROR

        )

        Button(onClick = {
            navigationRouter.returnBack()
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White)) {
            Text(text = "Save")
        }
    }


}