package cz.pef.project.ui.screens.garden_overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class GardenOverviewViewModel : ViewModel() {
    private val _uiState = mutableStateOf(GardenOverviewUiState())
    val uiState: GardenOverviewUiState get() = _uiState.value

    fun updateSearchQuery(query: String) {
        _uiState.value = uiState.copy(searchQuery = query)
        filterPlants()
    }

    private fun filterPlants() {
        val query = uiState.searchQuery.lowercase()
        _uiState.value = uiState.copy(
            filteredPlants = uiState.plants.filter {
                it.name.lowercase().contains(query) || it.description.lowercase().contains(query)
            }
        )
    }
}
