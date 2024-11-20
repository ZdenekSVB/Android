package cz.pef.project.ui.screens.flower_ai


import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FlowerAiViewModel : ViewModel() {
    private val _uiState = mutableStateOf(FlowerAiUiState())
    val uiState: FlowerAiUiState get() = _uiState.value

    fun selectImage() {
        // Simulace výběru obrázku
        _uiState.value = uiState.copy(
            selectedImageUri = Uri.parse("android.resource://com.example/drawable/sample_flower")
        )
    }

    fun analyzeImage() {
        if (uiState.selectedImageUri == null) return
        _uiState.value = uiState.copy(isLoading = true)

        // Simulace analýzy
        viewModelScope.launch {
            // Umělé zpoždění pro simulaci sítě nebo výpočtů
            kotlinx.coroutines.delay(2000)
            _uiState.value = uiState.copy(
                analysisResult = "This is a sunflower. It is healthy and thriving.",
                wikipediaUrl = "https://en.wikipedia.org/wiki/Sunflower",
                isLoading = false
            )
        }
    }

    fun openWikipedia() {
        val url = uiState.wikipediaUrl ?: return
        // Logika pro otevření URL
        // Například: Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }
}
