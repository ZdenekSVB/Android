package cz.mendelu.pef.xsvobo.projekt.ui.screens.playSet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaySetScreen(navigationRouter: INavigationRouter, id: Long) {

    var setData by remember { mutableStateOf(SetListScreenData()) }

    var cardData by remember {
        mutableStateOf(PlaySetScreenData())
    }

    val cards: MutableList<Card> = remember { mutableListOf() }

    val viewModel = hiltViewModel<PlaySetScreenViewModel>()

    val state = viewModel.playSetUIState.collectAsStateWithLifecycle()

    LaunchedEffect(state.value) {
        state.value.let {
            when (it) {
                is PlaySetScreenUIState.Loading -> {
                    viewModel.loadSet(id)
                }

                is PlaySetScreenUIState.Success -> {
                    if (cards.isEmpty()) {
                        cards.addAll(it.cards)
                    }
                    cardData.card = it.cards[cardData.index]
                }

                is PlaySetScreenUIState.CardAnswerChanged -> {
                    cardData = it.data
                }

                is PlaySetScreenUIState.Nextcard -> {
                    cardData = it.data
                }

                is PlaySetScreenUIState.LoadSet -> {
                    setData = it.data
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = {
                    navigationRouter.returnBack()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                }
            }, title = {
                Text(text = setData.set.name)
            })
        },
    ) { paddingValues ->
        PlaySetScreenContent(
            paddingValues = paddingValues,
            navigationRouter = navigationRouter,
            cardData = cardData,
            viewModel = viewModel,
            id = id,
            cards = cards
        )
    }
}

@Composable
fun PlaySetScreenContent(
    paddingValues: PaddingValues,
    navigationRouter: INavigationRouter,
    cardData: PlaySetScreenData,
    viewModel: PlaySetScreenActions,
    id: Long,
    cards: List<Card>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(text = cardData.card.question ?: "")
        }
        TextField(
            value = cardData.card.answer ?: "",
            onValueChange = {
                viewModel.cardAnswerChanged(it)
            },
        )
        Button(
            onClick = {
                if (cardData.card.answer == cardData.card.rightAnswer) {
                    viewModel.incrementCorrectCount()
                }
                if (cardData.index == cards.size - 1) {
                    navigationRouter.navigateToResultsScreen(id, cardData.correctCount)
                } else {
                    viewModel.nextCard()
                }
            }, colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, contentColor = Color.White
            )
        ) {
            Text(text = stringResource(id = R.string.next))
        }
    }
}
