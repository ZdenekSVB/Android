package cz.pef.project.ui.screens.flower_location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import cz.pef.project.communication.CommunicationResult
import cz.pef.project.communication.GardenRemoteRepositoryImpl
import cz.pef.project.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlowerLocationViewModel @Inject constructor(
    private val gardenRemoteRepository: GardenRemoteRepositoryImpl,
    private val plantDao: UserDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(FlowerLocationUiState())
    val uiState: StateFlow<FlowerLocationUiState> = _uiState

    init {
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
                        selectedPlant = plant,
                        location = LatLng(plant.latitude, plant.longitude) // Aktualizace pozice
                    )
                } else {
                    _uiState.value = _uiState.value.copy(error = Error("Plant not found"))
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = Error("Failed to load plant details: ${e.message}"))
            }
        }
    }

    fun updateLocation(newLocation: LatLng) {
        _uiState.value = _uiState.value.copy(location = newLocation)
    }


    fun saveLocation(plantId: Int) {
        viewModelScope.launch {
            try {

                Log.d("ABC saveFun",uiState.value.location.latitude.toString()+" "+uiState.value.location.longitude.toString())

                val location = _uiState.value.location
                plantDao.updatePlantLocation(
                    plantId = plantId,
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(cException = e)
            }
        }
    }


    // Další logika pro aktualizaci lokaci
}
