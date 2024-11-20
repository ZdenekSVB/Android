package cz.pef.project.ui.screens.flower_description

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FlowerDescriptionViewModel : ViewModel() {
    private val _uiState = mutableStateOf(FlowerDescriptionUiState())
    val uiState: FlowerDescriptionUiState get() = _uiState.value

    fun showEditNameDialog() {
        _uiState.value = uiState.copy(isEditNameDialogVisible = true)
    }

    fun showEditDatesDialog() {
        _uiState.value = uiState.copy(isEditDatesDialogVisible = true)
    }

    fun updateDescription(description: String) {
        _uiState.value = uiState.copy(description = description)
    }

    fun updateName(newName: String) {
        _uiState.value = uiState.copy(name = newName)
    }

    fun updateDates(plantDate: String?, deathDate: String?) {
        _uiState.value = uiState.copy(plantDate = plantDate, deathDate = deathDate)
    }
}
