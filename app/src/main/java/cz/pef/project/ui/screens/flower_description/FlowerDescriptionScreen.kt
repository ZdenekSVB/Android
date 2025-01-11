package cz.pef.project.ui.screens.flower_description

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.pef.project.communication.Plant
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.elements.FlowerAppBar
import cz.pef.project.ui.elements.FlowerNavigationBar

@Composable
fun FlowerDescriptionScreen(navigation: INavigationRouter,id: Int) {
    val viewModel = hiltViewModel<FlowerDescriptionViewModel>()
    val uiState = viewModel.uiState

    MaterialTheme(
        colorScheme = if (true) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = { FlowerAppBar(title = "Description", navigation = navigation) },
            bottomBar = { FlowerNavigationBar(navigation = navigation, selectedItem = "Description",id=id) },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    // Jméno kytky s edit tlačítkem
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = uiState.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        IconButton(onClick = { viewModel.showEditNameDialog() }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Name")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tabulka s daty zasazení a úmrtí
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Plant Date: ${uiState.plantDate ?: "N/A"}")
                            Text("Death Date: ${uiState.deathDate ?: "N/A"}")
                        }
                        IconButton(onClick = { viewModel.showEditDatesDialog() }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Edit Dates")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Popis kytky
                    OutlinedTextField(
                        value = uiState.description,
                        onValueChange = { viewModel.updateDescription(it) },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .verticalScroll(rememberScrollState())
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Výsledky zdraví
                    Text(
                        text = "Health Results:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(uiState.healthResults) { result ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Result ${result.number}:",
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = result.condition,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
