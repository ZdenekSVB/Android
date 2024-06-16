package cz.mendelu.pef.xsvobo.projekt.ui.screens.setList

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.set.ILocalSetsRepository
import cz.mendelu.pef.xsvobo.projekt.datastore.SetPreferences
import cz.mendelu.pef.xsvobo.projekt.model.Set
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SetListScreenViewModel @Inject constructor(
    private val repositorySets: ILocalSetsRepository,
    private val setPreferences: SetPreferences,
    @ApplicationContext private val context: Context
) : ViewModel(), SetListScreenActions {

    var sets: MutableList<Set> = mutableListOf()

    val setListScreenUIState: MutableState<SetListScreenUIState> = mutableStateOf(
        SetListScreenUIState.Loading()
    )

    private var setData: SetListScreenData = SetListScreenData()

    private val _setListScreenUIState: MutableStateFlow<SetListScreenUIState> =
        MutableStateFlow(value = SetListScreenUIState.Loading())

    private val _setIconUrls: MutableStateFlow<Map<Long, String?>> = MutableStateFlow(emptyMap())
    val setIconUrls: StateFlow<Map<Long, String?>> = _setIconUrls

    init {
        viewModelScope.launch {
            repositorySets.getAll().collect { sets ->
                val iconMap = sets.associate { it.id!! to it.icon }
                _setIconUrls.value = iconMap
            }
        }
    }
    override fun createSet() {
        viewModelScope.launch {
            if (setData.set.id == null) {
                setData.set.name = "Set"
                val setId = repositorySets.insert(setData.set)
                setData.set = repositorySets.getSet(setId)
                setData.set.id?.let { setId ->
                    setPreferences.saveSetId(setId)
                }
                _setListScreenUIState.value = SetListScreenUIState.Success(sets)
            }
        }
    }

    override fun deleteSet(setId: Long) {
        viewModelScope.launch {
            val setToDelete = repositorySets.getSet(setId)
            if (setToDelete != null) {
                val numberOfDeleted = repositorySets.delete(setToDelete)
                if (numberOfDeleted > 0) {
                    _setListScreenUIState.update {
                        SetListScreenUIState.SetDeleted()
                    }
                    setPreferences.clearIconUrl(setId)
                }
            }
        }
    }

    fun loadSets() {
        viewModelScope.launch {
            repositorySets.getAll().collect { sets ->
                setListScreenUIState.value = SetListScreenUIState.Success(sets)
            }
        }
    }

    override fun updateIcon(uri: Uri, setId: Long) {
        viewModelScope.launch {
            val fileName = saveImageToInternalStorage(uri)
            repositorySets.updateSetIcon(setId, fileName)
            _setIconUrls.update { it + (setId to fileName) }
            setPreferences.saveIconUrl(setId, fileName)
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "JPEG_${timeStamp}_.jpg"
        val file = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return fileName
    }
}
