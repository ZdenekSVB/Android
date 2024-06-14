package cz.mendelu.pef.xsvobo.projekt.ui.screens.results

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.pef.xsvobo.projekt.R
import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navigationRouter: INavigationRouter, setId: Long, correctCount: Int) {

    val cards: MutableList<Card> = remember { mutableListOf() }

    val viewModel = hiltViewModel<ResultsScreenViewModel>()

    val state = viewModel.resultsScreenUIState.collectAsStateWithLifecycle()

    LaunchedEffect(state.value) {
        state.value.let {
            when (it) {
                is ResultsScreenUIState.Loading -> {
                    viewModel.loadSet(setId)
                }

                is ResultsScreenUIState.Success -> {
                    if (cards.isEmpty()) {
                        cards.addAll(it.cards)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = {
                    navigationRouter.navigateToMenuScreen(null)
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }

            }, title = {
                Text(text = stringResource(id = R.string.results_title))
            })
        },
    ) {

        ResultsScreenContent(
            paddingValues = it, cards = cards, correctCount = correctCount
        )


    }


}

@Composable
fun ResultsScreenContent(
    paddingValues: PaddingValues, cards: List<Card>, correctCount: Int
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Set the background color to gray
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.set_completed))
            //Icon(imageVector = Icons.Default.Check, contentDescription = "")
        }
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            //TODO CHART
        }
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.correct_count)+" ${correctCount} / ${cards.size}")
        }
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.wrong_count)+" ${cards.size - correctCount} / ${cards.size}")
        }

    }


}