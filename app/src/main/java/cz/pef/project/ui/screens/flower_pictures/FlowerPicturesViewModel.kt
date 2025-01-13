package cz.pef.project.ui.screens.flower_pictures

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.project.DB.PictureEntity
import cz.pef.project.communication.Picture
import cz.pef.project.dao.UserDao
import cz.pef.project.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlowerPicturesViewModel @Inject constructor(
    private val pictureDao: UserDao,
    val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(FlowerPicturesUiState())
    val uiState: StateFlow<FlowerPicturesUiState> get() = _uiState

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme

    init {
        viewModelScope.launch {
            observeThemePreference()
        }
    }


    fun loadPicturesFromDatabase(plantId: Int) {
        viewModelScope.launch {
            try {
                val pictures = pictureDao.getPicturesByPlantId(plantId)
                val pictureList = pictures.map { Picture(url = it.url, name = it.name) }
                _uiState.value = _uiState.value.copy(pictures = pictureList)
            } catch (e: Exception) {
                setError(e)
                Log.e("FlowerPicturesVM", "Chyba při načítání obrázků: ${e.message}")
            }
        }
    }


    fun addPictureToPlant(pictureUri: String, plantId: Int, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Kontrola, zda obrázek již existuje
                val existingPicture = pictureDao.getPictureByUriAndPlantId(pictureUri, plantId)
                if (existingPicture != null) {
                    onError("Foto je již přidáno do galerie")
                    return@launch
                }

                // Uložení obrázku
                val newPictureEntity = PictureEntity(
                    plantId = plantId,
                    url = pictureUri,
                    name = "Obrázek ${System.currentTimeMillis()}"
                )
                pictureDao.insertPicture(newPictureEntity)

                // Aktualizace UI stavu
                val newPicture = Picture(url = pictureUri, name = newPictureEntity.name)
                _uiState.value = _uiState.value.copy(
                    pictures = _uiState.value.pictures + newPicture
                )
            } catch (e: Exception) {
                onError("Obrázek se nepodařilo přidat: ${e.message}")
            }
        }
    }



    fun setError(error: Throwable) {
        _uiState.value = _uiState.value.copy(error = error)
    }

    fun updatePictureName(pictureUrl: String, newName: String) {
        if (newName.isBlank()) {
            Log.e("FlowerPicturesVM", "Nový název nesmí být prázdný")
            return
        }
        viewModelScope.launch {
            try {
                val pictureEntity = pictureDao.getPictureByUrl(pictureUrl)
                if (pictureEntity != null) {
                    val updatedPicture = pictureEntity.copy(name = newName)
                    pictureDao.updatePicture(updatedPicture)

                    val updatedPictures = _uiState.value.pictures.map {
                        if (it.url == pictureUrl) it.copy(name = newName) else it
                    }
                    _uiState.value = _uiState.value.copy(pictures = updatedPictures)
                } else {
                    Log.e("FlowerPicturesVM", "Obrázek nenalezen: $pictureUrl")
                }
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    fun deletePicture(pictureUrl: String) {
        viewModelScope.launch {
            try {
                pictureDao.deletePictureByUrl(pictureUrl)

                val updatedPictures = _uiState.value.pictures.filterNot { it.url == pictureUrl }
                _uiState.value = _uiState.value.copy(pictures = updatedPictures)
            } catch (e: Exception) {
                setError(e)
            }
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
