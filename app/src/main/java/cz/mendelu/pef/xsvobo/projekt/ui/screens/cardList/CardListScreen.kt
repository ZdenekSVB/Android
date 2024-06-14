package cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.model.Set
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenData
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    navigationRouter: INavigationRouter,
    id: Long
) {
    var setData by remember {
        mutableStateOf(SetListScreenData())
    }
    val cards: MutableList<Card> = mutableListOf()

    val viewModel = hiltViewModel<CardListScreenViewModel>()

    val state = viewModel.cardListScreenUIState.collectAsStateWithLifecycle()


    state.value.let {
        when (it) {
            is CardListScreenUIState.Loading -> {
                viewModel.loadSet(id)
            }

            is CardListScreenUIState.Success -> {
                cards.addAll(it.cards)
            }

            is CardListScreenUIState.SetNameChanged -> {
                setData = it.data
            }

            is CardListScreenUIState.CardDeleted -> {
                viewModel.loadSet(id)
            }

        }

    }

    Log.d("Načtení CardListScreen", "Velikost Baličku: ${cards.size}")
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
                    Text(
                        text = "Card List",
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    setData.set.id?.let { viewModel.addCard(it) }
                },
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = "") }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {
        CardListScreenContent(
            paddingValues = it,
            cards = cards,
            navigationRouter = navigationRouter,
            setData = setData,
            actions=viewModel
        )
    }
}

@Composable
fun CardListScreenContent(
    paddingValues: PaddingValues,
    cards: List<Card>,
    navigationRouter: INavigationRouter,
    setData: SetListScreenData,
    actions: CardListScreenActions
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Set the background color to gray
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.LightGray), // Set the background color to gray
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color.White)
                    .padding(16.dp)
            ) {
                Text(text = "Cards: ${cards.size}")
            }

            TextField(
                value = setData.set.name,
                onValueChange = {
                    actions.setTextChanged(it)
                }, leadingIcon = {
                    IconButton(onClick = { actions.saveSetName() }) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "")
                    }
                })
        }
        Spacer(modifier = Modifier.height(16.dp))


        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(text = "Set Generated Code")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            cards.forEach {
                item {
                    CardListRow(
                        card = it,
                        navigationRouter = navigationRouter,
                        actions = actions
                    )
                }
            }
        }
    }
}

//TODO dodělat podle předlohy
@Composable
fun CardListRow(
    card: Card,
    navigationRouter: INavigationRouter,
    actions: CardListScreenActions
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
            }
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = card.name.substring(0, 1),
                    modifier = Modifier
                        .padding(16.dp)
                        .drawBehind {
                            drawCircle(
                                color = Color.hsl(230F, 0.89F, 0.64F),
                                radius = this.size.maxDimension
                            )
                        },
                    color = Color.White,
                )
            }
            Column {
                Text(text = "${card.name} ${card.id} ")
            }
        }
        Column {
            Row {
                IconButton(onClick = {
                    card.id?.let { actions.deleteCard(it) }
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
                IconButton(onClick = {
                    navigationRouter.navigateToAddCardScreen(card.id!!)
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                }
            }
        }
    }

}
