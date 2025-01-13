package cz.pef.project.ui.screens.flower_location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import cz.pef.project.communication.CommunicationResult
import cz.pef.project.communication.GardenRemoteRepositoryImpl
import cz.pef.project.dao.UserDao
import cz.pef.project.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlowerLocationViewModel @Inject constructor(
    private val gardenRemoteRepository: GardenRemoteRepositoryImpl,
    private val plantDao: UserDao,
    val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(FlowerLocationUiState())
    val uiState: StateFlow<FlowerLocationUiState> = _uiState

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme


    init {
        viewModelScope.launch {
            observeThemePreference()
        }
        loadGardenCenters()
    }

    private fun loadGardenCenters() {
        viewModelScope.launch {
            when (val result = gardenRemoteRepository.getAllGardenCenters()) {
                is CommunicationResult.Success -> {
                    _uiState.value = _uiState.value.copy(gardenCenters = result.data.features)
                }

                is CommunicationResult.Exception -> {
                    _uiState.value = _uiState.value.copy(exception = result.exception)
                }

                else -> {}
            }
        }
    }

    fun loadPlantDetails(plantId: Int) {
        viewModelScope.launch {
            try {
                val plant = plantDao.getPlantById(plantId)
                if (plant != null) {
                    _uiState.value = _uiState.value.copy(
                        selectedPlant = plant, location = LatLng(plant.latitude, plant.longitude)
                    )
                }
            } catch (e: Exception) {
                Log.e("FlowerLocationViewModel", "Failed to load plant details", e)
            }
        }
    }

    fun updatePlantLocation(plantId: Int, newLocation: LatLng) {
        viewModelScope.launch {
            try {
                plantDao.updatePlantLocation(plantId, newLocation.latitude, newLocation.longitude)
                Log.d("FlowerLocationViewModel", "Location updated for plantId $plantId: $newLocation")

                // Načtení aktuálních detailů rostliny
                loadPlantDetails(plantId)
            } catch (e: Exception) {
                Log.e("FlowerLocationViewModel", "Failed to update plant location", e)
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
