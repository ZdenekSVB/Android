package cz.pef.project.ui.screens.flower_ai

import android.net.Uri

data class FlowerAiUiState(
    val selectedImageUri: Uri? = null,
    val analysisResult: String? = null
)
