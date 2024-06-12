package cz.mendelu.pef.xsvobo.projekt.ui.screens.setList

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.set.ILocalSetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetListScreenViewModel @Inject constructor(private val repository: ILocalSetsRepository) :
    ViewModel(), SetListScreenActions {

    val setListScreenUIState: MutableState<SetListScreenUIState> = mutableStateOf(
        SetListScreenUIState.Loading()
    )

    private var data: SetListScreenData = SetListScreenData()

    private val _addCardScreenUIState: MutableStateFlow<SetListScreenUIState> =
        MutableStateFlow(value = SetListScreenUIState.Loading())

    override fun createSet() {
            viewModelScope.launch {
                if (data.set.id == null) {
                    data.set.name="Set"
                    repository.insert(data.set)
                }
            }
    }

    override fun deleteSet(setId:Long) {
        viewModelScope.launch {
            val numberOfDeleted = repository.delete(repository.getSet(setId))
            if (numberOfDeleted > 0) {
                _addCardScreenUIState.update {
                    SetListScreenUIState.SetDeleted()
                }
            }
        }
    }

    fun loadSets() {
        viewModelScope.launch {
            repository.getAll().collect {
                setListScreenUIState.value = SetListScreenUIState.Success(it)
            }
        }
    }


}