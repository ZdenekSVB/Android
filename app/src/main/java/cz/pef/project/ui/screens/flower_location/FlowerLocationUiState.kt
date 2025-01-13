package cz.pef.project.ui.screens.flower_location

import com.google.android.gms.maps.model.LatLng
import cz.pef.project.DB.PlantEntity
import cz.pef.project.communication.Feature

data class FlowerLocationUiState(
    val id: Int = 0,
    val location: LatLng? = null,
    val gardenCenters: List<Feature> = emptyList(),
    val selectedPlant: PlantEntity? = null,
    val error: Error? = null,
    val exception: Throwable? = null,
    val cException: Exception? = null,

    )
