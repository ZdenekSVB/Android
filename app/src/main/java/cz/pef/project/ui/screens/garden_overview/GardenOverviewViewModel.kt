package cz.pef.project.ui.screens.garden_overview

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import cz.pef.project.DB.PlantEntity
import cz.pef.project.DB.ResultEntity
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

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme

    init {
        viewModelScope.launch {
            try {
                observeThemePreference()
                _searchQuery.collect { query -> filterPlants(query) }
            } catch (e: Exception) {
                Log.e("GardenOverviewViewModel", "Error initializing ViewModel: ${e.message}", e)
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
        _uiState.value = uiState.value.copy(
            searchQuery = query, // Synchronizace searchQuery v uiState
            filteredPlants = filtered
        )
    }


    fun addPlant(userId: Int?, name: String) {
        if (userId == null) {
            Log.e("GardenOverviewViewModel", "Cannot add plant: userId is null")
            return
        }
        viewModelScope.launch {
            try {
                val newPlant = PlantEntity(
                    userId = userId,
                    name = name,
                    description = null,
                    lastCondition = null,
                    plantedDate = null,
                    deathDate = null,
                    latitude = 50.00,
                    longitude = 14.00
                )
                plantDao.insertPlant(newPlant)
                loadPlants(userId) // Reload plants after insertion
            } catch (e: Exception) {
                Log.e("GardenOverviewViewModel", "Error adding plant: ${e.message}", e)
            }
        }
    }


    fun loadPlants(userId: Int) {
        viewModelScope.launch {
            try {
                val plants = plantDao.getPlantsByUserId(userId)
                if (plants.isEmpty()) {
                    Log.w("GardenOverviewViewModel", "No plants found for userId: $userId")
                }
                _uiState.value = _uiState.value.copy(
                    plants = plants.map { it.toPlant() },
                    filteredPlants = plants.map { it.toPlant() }
                )
            } catch (e: Exception) {
                Log.e("GardenOverviewViewModel", "Error loading plants: ${e.message}", e)
            }
        }
    }


    fun getResultForPlant(plantId: Int) {
        viewModelScope.launch {
            try {
                val result = plantDao.getLastResultByPlantId(plantId)
                if (result == null) {
                    Log.w("GardenOverviewViewModel", "No result found for plantId: $plantId")
                } else {
                    Log.d("GardenOverviewViewModel", "Result for plantId: $plantId: $result")
                }
            } catch (e: Exception) {
                Log.e("GardenOverviewViewModel", "Error fetching results for plantId: $plantId", e)
            }
        }
    }


    private fun observeThemePreference() {
        viewModelScope.launch {
            dataStoreManager.darkModeFlow.collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
    }




}
