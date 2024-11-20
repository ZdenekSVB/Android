package cz.pef.project.ui.screens.garden_overview

import cz.pef.project.communication.Plant

data class GardenOverviewUiState(
    val plants: List<Plant> = listOf(),
    val filteredPlants: List<Plant> = plants,
    val searchQuery: String = ""
)
