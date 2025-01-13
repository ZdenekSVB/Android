package cz.pef.project.ui.screens.garden_overview

import cz.pef.project.communication.Plant

data class GardenOverviewUiState(
    val plants: List<Plant> = emptyList(),
    val filteredPlants: List<Plant> = emptyList(),
    val searchQuery: String = "", // Přidejte searchQuery
)
