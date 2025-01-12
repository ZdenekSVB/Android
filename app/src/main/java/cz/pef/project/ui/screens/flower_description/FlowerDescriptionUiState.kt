package cz.pef.project.ui.screens.flower_description

data class FlowerDescriptionUiState(
    val id: Int = 0,
    val name: String? = null,
    val description: String? = null,
    val plantDate: String? = null,
    val deathDate: String? = null,
    val isEditNameDialogVisible: Boolean = false,
    val isEditDatesDialogVisible: Boolean = false,
    val isEditingDescription: Boolean = false // Nový stav pro režim editace
)


data class HealthResult(
    val number: Int,
    val condition: String
)
