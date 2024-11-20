package cz.pef.project.ui.screens.flower_pictures

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.project.communication.Picture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlowerPicturesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FlowerPicturesUiState())
    val uiState: StateFlow<FlowerPicturesUiState> get() = _uiState

    init {
        // Načítání obrázků při inicializaci
        viewModelScope.launch {
            loadPictures(
                listOf(
                    Picture("https://example.com/image1.jpg", "Flower 1"),
                    Picture("https://example.com/image2.jpg", "Flower 2"),
                    Picture("https://example.com/image3.jpg", "Flower 3")
                )
            )
        }
    }

    private fun loadPictures(pictures: List<Picture>) {
        _uiState.value = FlowerPicturesUiState(pictures = pictures)
    }

    fun setError(error: Throwable) {
        _uiState.value = _uiState.value.copy(error = error)
    }
}
