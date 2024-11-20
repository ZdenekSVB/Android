package cz.pef.project.ui.screens.flower_description

data class FlowerDescriptionUiState(
    val name: String = "Sunflower",
    val plantDate: String? = null,
    val deathDate: String? = null,
    val description: String = "This is a beautiful flower.",
    val healthResults: List<HealthResult> = listOf(
        HealthResult(1, "Healthy"),
        HealthResult(2, "Needs Water"),
        HealthResult(3, "Good Condition")
    ),
    val isEditNameDialogVisible: Boolean = false,
    val isEditDatesDialogVisible: Boolean = false
)

data class HealthResult(
    val number: Int,
    val condition: String
)
