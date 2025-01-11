import cz.pef.project.ui.screens.garden_overview.GardenOverviewViewModel

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import coil3.compose.rememberAsyncImagePainter
import cz.pef.project.communication.Plant
import cz.pef.project.datastore.DataStoreManager
import cz.pef.project.navigation.INavigationRouter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenOverviewScreen(navigation: INavigationRouter) {
    val darkTheme = true
    val context = LocalContext.current
    val viewModel = hiltViewModel<GardenOverviewViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val dataStore = hiltViewModel<GardenOverviewViewModel>().dataStoreManager

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
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AI Garden Helper") },
                    navigationIcon = {
                        IconButton(onClick = { navigation.returnBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { navigation.navigateToUserSettingsScreen() }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "User Settings")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    actions = {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = { Text("Search") },
                            modifier = Modifier.weight(1f),
                            trailingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                        )
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { setShowDialog(true) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Plant")
                }
            },
            content = { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(8.dp),
                    contentPadding = PaddingValues(bottom = 72.dp) // Space for BottomAppBar
                ) {
                    items(uiState.filteredPlants) { plant -> // Použijte filteredPlants
                        PlantCard(
                            plant = plant,
                            onClick = { /* Navigate to plant details */ }
                        )
                    }
                }

            }
        )

        // Add Plant Dialog
        if (showDialog) {
            AddPlantDialog(
                onDismiss = { setShowDialog(false) },
                onAdd = { name, description, plantedDate ->
                    viewModel.viewModelScope.launch {
                        val username = dataStore.getLoginState().firstOrNull()?.second
                        if (!username.isNullOrEmpty()) {
                            val userId = dataStore.getUserId(username)
                            if (userId != null) {
                                viewModel.addPlant(userId, name, description, plantedDate)
                            } else {
                                Log.e("GardenOverviewScreen", "User ID is null")
                            }
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
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(plant.imageUrl),
                contentDescription = plant.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plant.name, style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = plant.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (plant.isHealthy) "Healthy" else "Not Healthy",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (plant.isHealthy) Color.Green else Color.Red
                )
            }
        }
    }
}

@Composable
fun AddPlantDialog(
    onDismiss: () -> Unit, onAdd: (name: String, description: String, plantedDate: String) -> Unit
) {
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val plantedDate = remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add New Plant", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Name") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = plantedDate.value,
                    onValueChange = { plantedDate.value = it },
                    label = { Text("Planted Date") })
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.End) {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onAdd(name.value, description.value, plantedDate.value)
                        onDismiss()
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}