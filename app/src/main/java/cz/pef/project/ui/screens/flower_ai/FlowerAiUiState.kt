package cz.pef.project.ui.screens.flower_ai

import android.net.Uri

data class FlowerAiUiState(
    val selectedImageUri: Uri? = null,
    val analysisResult: String? = null,
    val wikipediaUrl: String? = null,
    val isLoading: Boolean = false
)
