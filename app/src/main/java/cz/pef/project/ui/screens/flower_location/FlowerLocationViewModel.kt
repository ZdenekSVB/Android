package cz.pef.project.ui.screens.flower_location

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FlowerLocationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FlowerLocationUiState())
    val uiState: StateFlow<FlowerLocationUiState> = _uiState

    fun updateLocation(newLocation: LatLng) {
        _uiState.value = _uiState.value.copy(location = newLocation)
    }
}