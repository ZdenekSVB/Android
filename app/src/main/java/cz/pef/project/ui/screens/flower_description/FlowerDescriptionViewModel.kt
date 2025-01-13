package cz.pef.project.ui.screens.flower_description

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.project.DB.ResultEntity
import cz.pef.project.dao.UserDao
import cz.pef.project.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlowerDescriptionViewModel @Inject constructor(
    private val plantDao: UserDao,
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiState = mutableStateOf(FlowerDescriptionUiState())
    val uiState: FlowerDescriptionUiState get() = _uiState.value

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme


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

    fun loadPlantDetails(plantId: Int) {
        viewModelScope.launch {
            val plant = plantDao.getPlantById(plantId)
            if (plant != null) {
                _uiState.value = uiState.copy(
                    id = plant.id, // Uložení ID
                    name = plant.name,
                    description = plant.description,
                    plantDate = plant.plantedDate,
                    deathDate = plant.deathDate
                )
            }
        }
    }

    fun dismissDialogs() {
        _uiState.value = uiState.copy(
            isEditNameDialogVisible = false,
            isEditDatesDialogVisible = false
        )
    }
    fun saveNameToDatabase(newName: String) {
        viewModelScope.launch {
            uiState.name?.let {
                val plantId = uiState.id
                val plant = plantDao.getPlantById(plantId)
                if (plant != null) {
                    plant.name = newName
                    plantDao.updatePlant(plant)
                }
            }
        }
    }

    fun saveDatesToDatabase(plantDate: String?, deathDate: String?) {
        viewModelScope.launch {
            val plantId = uiState.id
            val plant = plantDao.getPlantById(plantId)
            if (plant != null) {
                plant.plantedDate = plantDate
                plant.deathDate = deathDate
                plantDao.updatePlant(plant)
            }
        }
    }

    fun toggleDescriptionEdit() {
        _uiState.value = uiState.copy(isEditingDescription = !uiState.isEditingDescription)
    }

    fun saveDescriptionToDatabase() {
        viewModelScope.launch {
            val plantId = uiState.id
            val plant = plantDao.getPlantById(plantId)
            if (plant != null) {
                plant.description = uiState.description
                plantDao.updatePlant(plant)
            }
        }
    }
    fun loadResultsForPlant(plantId: Int) {
        viewModelScope.launch {
            val results = plantDao.getResultsByPlantId(plantId).mapIndexed { index, result ->
                HealthResult(number = index+1, condition = result.condition, description = result.description)
            }
            _uiState.value = uiState.copy(results = results)
        }
    }

    private fun observeThemePreference() {
        viewModelScope.launch {
            dataStoreManager.darkModeFlow.collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
    }
}
