package cz.mendelu.pef.xsvobo.projekt.ui.screens.setList

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.pef.xsvobo.projekt.model.Set
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetListScreen(navigationRouter: INavigationRouter) {

    val sets: MutableList<Set> = mutableListOf()


    val viewModel = hiltViewModel<SetListScreenViewModel>()

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
                        text = "Set List",
                    )
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
        SetListScreenContent(
            paddingValues = it,
            sets = sets,
            navigationRouter = navigationRouter,
            viewModel = viewModel
        )
    }
}

@Composable
fun SetListScreenContent(
    paddingValues: PaddingValues,
    sets: List<Set>,
    navigationRouter: INavigationRouter,
    viewModel: SetListScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Set the background color to gray
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            sets.forEach {
                item {
                    SetListRow(
                        set = it,
                        navigationRouter = navigationRouter,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun SetListRow(
    set: Set,
    navigationRouter: INavigationRouter,
    viewModel: SetListScreenViewModel
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
                Text(text = "${set.name} ${set.id} ")
            }
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
                    navigationRouter.navigateToPlaySetScreen(set.id!!)
                }) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "")
                }
            }
        }
    }

}