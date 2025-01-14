package cz.pef.project.ui.screens.garden_overview

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.project.R
import cz.pef.project.communication.Plant
import cz.pef.project.navigation.INavigationRouter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenOverviewScreen(navigation: INavigationRouter) {
    val viewModel = hiltViewModel<GardenOverviewViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState() // Watch the dark theme setting
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val dataStore = viewModel.dataStoreManager

    // Fetch user data and load plants
    LaunchedEffect(Unit) {
        val loginState = dataStore.getLoginState().firstOrNull()
        val username = loginState?.second
        if (!username.isNullOrEmpty()) {
            val userId = dataStore.getUserId(username) ?: return@LaunchedEffect
            viewModel.loadPlants(userId)
        } else {
            Log.e("GardenOverviewScreen", "Username is null or empty")
        }
    }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.ai_garden_helper)) },
                    navigationIcon = {
                        IconButton(onClick = { navigation.returnBack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.back),
                                modifier = Modifier.testTag("BackButton")
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navigation.navigateToUserSettingsScreen() }) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = stringResource(id = R.string.user_settings),
                                modifier = Modifier.testTag("UserSettingsButton")
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    actions = {
                        OutlinedTextField(
                            value = uiState.searchQuery, // Value linked to state
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = { Text(stringResource(id = R.string.search)) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("SearchField"),
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                        Icon(
                                            Icons.Default.Clear,
                                            contentDescription = stringResource(id = R.string.clear),
                                            modifier = Modifier.testTag("ClearSearchButton")
                                        )
                                    }
                                } else {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = stringResource(id = R.string.search),
                                        modifier = Modifier.testTag("SearchIcon")
                                    )
                                }
                            }
                        )
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { setShowDialog(true) }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_plant),
                        modifier = Modifier.testTag("AddPlantButton")
                    )
                }
            },
            content = { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(8.dp)
                        .testTag("PlantList"),
                    contentPadding = PaddingValues(bottom = 72.dp) // Space for BottomAppBar
                ) {
                    items(uiState.filteredPlants) { plant ->
                        PlantCard(
                            plant = plant,
                            onClick = { navigation.navigateToFlowerDescriptionScreen(plant.id) }
                        )
                    }
                }
            }
        )

        // Add Plant Dialog
        if (showDialog) {
            AddPlantDialog(
                onDismiss = { setShowDialog(false) },
                onAdd = { name ->
                    viewModel.viewModelScope.launch {
                        val username = dataStore.getLoginState().firstOrNull()?.second
                        if (!username.isNullOrEmpty()) {
                            val userId = dataStore.getUserId(username)
                            viewModel.addPlant(userId, name)
                        } else {
                            Log.e("GardenOverviewScreen", "Username is null or empty")
                        }
                        setShowDialog(false)
                    }
                }
            )
        }
    }
}


@Composable
fun PlantCard(plant: Plant, onClick: () -> Unit) {
    // Určení barvy na základě podmínky rostliny
    val conditionColor = when {
        plant.lastCondition.contains("Healthy", ignoreCase = true) -> Color.Green
        plant.lastCondition.contains("Unknown", ignoreCase = true) -> Color.Gray
        else -> Color.Red
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Zobrazení textových informací
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(plant.name, style = MaterialTheme.typography.titleMedium)
                // Zobrazení podmínky s barvou
                Text(
                    stringResource(id = R.string.condition, plant.lastCondition),
                    color = conditionColor // Nastavení barvy podle podmínky
                )
            }
        }
    }
}

@Composable
fun AddPlantDialog(
    onDismiss: () -> Unit, onAdd: (name: String) -> Unit
) {
    val name = remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    stringResource(id = R.string.add_new_plant),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text(stringResource(id = R.string.name)) })
                Row(horizontalArrangement = Arrangement.End) {
                    Button(onClick = onDismiss) {
                        Text(stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onAdd(name.value)
                        onDismiss()
                    }) {
                        Text(stringResource(id = R.string.add))
                    }
                }
            }
        }
    }
}
