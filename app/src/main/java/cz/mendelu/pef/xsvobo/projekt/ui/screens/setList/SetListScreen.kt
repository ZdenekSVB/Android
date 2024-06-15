package cz.mendelu.pef.xsvobo.projekt.ui.screens.setList

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.pef.xsvobo.projekt.R
import cz.mendelu.pef.xsvobo.projekt.model.Set
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetListScreen(navigationRouter: INavigationRouter) {
    val sets: MutableList<Set> = mutableListOf()
    val viewModel = hiltViewModel<SetListScreenViewModel>()
    var setData by remember { mutableStateOf(SetListScreenData()) }

    viewModel.setListScreenUIState.value.let {
        when (it) {
            is SetListScreenUIState.Loading -> {
                viewModel.loadSets()
            }

            is SetListScreenUIState.Success -> {
                sets.addAll(it.sets)
            }

            is SetListScreenUIState.SetDeleted -> {
                viewModel.loadSets()
            }

        }
    }

    val snackbarHostState = SnackbarHostState()

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
                    Text(text = stringResource(id = R.string.set_list_title))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.createSet()
                },
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = "") }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(it)
        ) {
            SetListContent(
                sets = sets,
                setData=setData,
                navigationRouter = navigationRouter,
                viewModel = viewModel,
                snackbarHostState = snackbarHostState
            )
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
    ) {
        Snackbar {
            Text(text = "Cannot play empty set")
        }
    }
}

@Composable
fun SetListContent(
    sets: List<Set>,
    setData: SetListScreenData,
    navigationRouter: INavigationRouter,
    viewModel: SetListScreenViewModel,
    snackbarHostState: SnackbarHostState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(sets) { set ->
            SetListRow(
                set = set,
                navigationRouter = navigationRouter,
                viewModel = viewModel,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@Composable
fun SetListRow(
    set: Set,
    navigationRouter: INavigationRouter,
    viewModel: SetListScreenViewModel,
    snackbarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Handle row click if needed
            }
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = set.name.substring(0, 1),
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
            Text(
                text = if (set.name != "Set") "${set.name} ${set.id}" else stringResource(id = R.string.set_name) + " ${set.id}"
            )
        }
        Column {
            Row {
                IconButton(onClick = {
                    set.id?.let { viewModel.deleteSet(it) }
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
                IconButton(onClick = {
                    navigationRouter.navigateToCardListScreen(set.id!!)
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                }
                IconButton(onClick = {
                    if (set.cardsCount <= 0) {
                        coroutineScope.launch {

                            Log.d("Returned", "Set: ${set.cardsCount}")
                            snackbarHostState.showSnackbar("Cannot play empty set")
                        }
                    } else if(set.cardsCount >0){
                        Log.d("Returned", "Set: ${set.cardsCount}")
                        navigationRouter.navigateToPlaySetScreen(set.id!!)
                    }
                }) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "")
                }
            }
        }
    }
}
