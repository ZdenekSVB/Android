package cz.pef.project.ui.screens.flower_location

import com.google.android.gms.maps.model.LatLng

data class FlowerLocationUiState(
    val location: LatLng = LatLng(50.0, 14.0), // Výchozí bod
    val error: Throwable? = null
)
