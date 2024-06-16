package cz.mendelu.pef.xsvobo.projekt.ui.screens.addCard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.pef.xsvobo.projekt.R
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    navigationRouter: INavigationRouter, id: Long?
) {


    val viewModel = hiltViewModel<AddCardScreenViewModel>()

    val state = viewModel.addCardUIState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(AddCardScreenData())
    }

    state.value.let {
        when (it) {
            is AddCardScreenUIState.Loading -> {
                viewModel.loadCard(id)
            }

            is AddCardScreenUIState.CardSaved -> {
                LaunchedEffect(it) {
                    navigationRouter.returnBack()
                }
            }

            is AddCardScreenUIState.CardDataChanged -> {
                data = it.data
            }

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = {
                    navigationRouter.returnBack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }

            }, title = {
                Text(text = stringResource(id = R.string.add_card))
            })
        },
    ) {

        AddCardScreenContent(
            paddingValues = it,
            navigationRouter = navigationRouter,
            actions = viewModel,
            cardData = data
        )


    }


}


@Composable
fun AddCardScreenContent(
    paddingValues: PaddingValues,
    navigationRouter: INavigationRouter,
    actions: AddCardScreenActions,
    cardData: AddCardScreenData
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Set the background color to gray
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = cardData.card.name, onValueChange = {
            cardData.card.name = it
            actions.cardTextChanged(it)
        }
        )

    TextField(label = { Text(text = stringResource(id = R.string.question)) },
        value = cardData.card.question + "",
        onValueChange = {
            actions.cardQuestionChanged(it)
        },
        isError = cardData.cardTextError != null,
        supportingText = {
            if (cardData.cardTextError != null) {
                Text(text = cardData.cardTextError!!)
            }
        }

    )
    TextField(label = { Text(text = stringResource(id = R.string.answer)) },
        value = cardData.card.rightAnswer + "",
        onValueChange = {
            actions.cardRightAnswerChanged(it)
        },
        isError = cardData.cardTextError != null,
        supportingText = {
            if (cardData.cardTextError != null) {
                Text(text = cardData.cardTextError!!)
            }
        })

    Button(
        onClick = {
            actions.saveCard()
        }, colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black, contentColor = Color.White
        )
    ) {
        Text(text = stringResource(id = R.string.save))
    }
    }
}
