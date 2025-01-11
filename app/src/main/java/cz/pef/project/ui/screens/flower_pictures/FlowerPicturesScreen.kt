package cz.pef.project.ui.screens.flower_pictures

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import cz.pef.project.communication.Plant
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.elements.FlowerAppBar
import cz.pef.project.ui.elements.FlowerNavigationBar

@Composable
fun FlowerPicturesScreen(navigation: INavigationRouter, id: Int) {
    val viewModel = hiltViewModel<FlowerPicturesViewModel>()
    val uiState = viewModel.uiState.collectAsState().value
    MaterialTheme(
        colorScheme = if (true) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = { FlowerAppBar(title = "Pictures", navigation = navigation) },
            bottomBar = {
                FlowerNavigationBar(
                    navigation = navigation,
                    selectedItem = "Pictures",
                    id = id
                )
            },
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when {
                    uiState.pictures.isEmpty() -> {
                        Text(
                            text = "No pictures available.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.pictures) { picture ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(picture.url),
                                        contentDescription = picture.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = picture.name,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
                uiState.error?.let { error ->
                    Text(
                        text = "Error: ${error.message}",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
