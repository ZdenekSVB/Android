package cz.pef.project.ui.screens.flower_pictures

import cz.pef.project.communication.Picture

data class FlowerPicturesUiState(
    val pictures: List<Picture> = emptyList(),
    val error: Throwable? = null
)
