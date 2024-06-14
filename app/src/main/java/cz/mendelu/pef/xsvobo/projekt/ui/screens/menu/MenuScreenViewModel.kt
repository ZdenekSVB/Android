package cz.mendelu.pef.xsvobo.projekt.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.set.ILocalSetsRepository
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuScreenViewModel @Inject constructor(private val repositorySet: ILocalSetsRepository) :
    ViewModel(), MenuScreenActions {

    private val _menuScreenUIState: MutableStateFlow<MenuScreenUIState> =
        MutableStateFlow(value = MenuScreenUIState.Loading())

    val menuScreenUIState = _menuScreenUIState.asStateFlow()

    fun loadLatestSet() {
        viewModelScope.launch {
            repositorySet.getLatestSet().collect {
                _menuScreenUIState.value = MenuScreenUIState.Success(it)
            }
        }
    }
}
