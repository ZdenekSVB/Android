package cz.mendelu.pef.xsvobo.projekt.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.set.ILocalSetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuScreenViewModel @Inject constructor(private val repositorySet: ILocalSetsRepository) :
    ViewModel(), MenuScreenActions {

    private val _menuScreenUIState: MutableStateFlow<MenuScreenUIState> =
        MutableStateFlow(value = MenuScreenUIState.Loading())

    val menuScreenUIState = _menuScreenUIState.asStateFlow()

    private val _setIconUrls: MutableStateFlow<Map<Long, String?>> = MutableStateFlow(emptyMap())
    val setIconUrls: StateFlow<Map<Long, String?>> = _setIconUrls

    init {
        viewModelScope.launch {
            repositorySet.getAll().collect { sets ->
                val iconMap = sets.associate { it.id!! to it.icon }
                _setIconUrls.value = iconMap
            }
        }
    }
    fun loadLatestSet() {
        viewModelScope.launch {
            repositorySet.getLatestSet().collect {
                _menuScreenUIState.value = MenuScreenUIState.Success(it)
            }
        }
    }
}
