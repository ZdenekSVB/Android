package cz.pef.project.ui.screens.garden_overview

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import cz.pef.project.DB.PlantEntity
import cz.pef.project.DB.toPlant
import cz.pef.project.dao.UserDao
import cz.pef.project.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

@HiltViewModel
class GardenOverviewViewModel @Inject constructor(
    private val plantDao: UserDao,
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(GardenOverviewUiState())
    val uiState: StateFlow<GardenOverviewUiState> get() = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    init {
        // Debounce search query to optimize filtering
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Adjust debounce time as needed
                .collect { query ->
                    filterPlants(query)
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun filterPlants(query: String) {
        val filtered = uiState.value.plants.filter {
            it.name.contains(query, ignoreCase = true)
        }
        _uiState.value = uiState.value.copy(filteredPlants = filtered)
    }

    fun addPlant(userId: Int, name: String, description: String, plantedDate: String) {
        viewModelScope.launch {
            try {
                val newPlant = PlantEntity(
                    userId = userId,
                    name = name,
                    description = description,
                    plantedDate = plantedDate,
                    deathDate = null
                )
                plantDao.insertPlant(newPlant)
                loadPlants(userId) // Reload plants after insertion
            } catch (e: Exception) {
                // Handle error (e.g., log it or show a message in the UI)
            }
        }
    }

    fun loadPlants(userId: Int) {
        viewModelScope.launch {
            try {
                val plants = plantDao.getPlantsByUserId(userId)
                _uiState.value = _uiState.value.copy(
                    plants = plants.map { it.toPlant() },
                    filteredPlants = plants.map { it.toPlant() } // Aktualizujte i filtrovaný seznam
                )
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

}
