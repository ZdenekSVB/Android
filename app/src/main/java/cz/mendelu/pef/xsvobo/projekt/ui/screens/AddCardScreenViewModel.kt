package cz.mendelu.pef.xsvobo.projekt.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.ILocalSetsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardScreenViewModel @Inject constructor(
    private val repository: ILocalSetsRepository
) : ViewModel(), AddCardScreenActions {

    private var data: AddCardScreenData = AddCardScreenData()

    private val _addCardScreenUIState: MutableStateFlow<AddCardScreenUIState> =
        MutableStateFlow(value = AddCardScreenUIState.Loading())

    val addEditSetUIState = _addCardScreenUIState.asStateFlow()

    override fun saveSet() {
        if (data.set.name.isNotEmpty()) {
            viewModelScope.launch {
                if (data.set.id == null) {
                    repository.insert(data.set)
                } else {
                    repository.update(data.set)
                }
                _addCardScreenUIState.update {
                    AddCardScreenUIState.SetSaved()
                }
            }
        } else {
            data.setTextError = "Cannot be empty"
            _addCardScreenUIState.update {
                AddCardScreenUIState.ScreenDataChanged(data)
            }
        }

        // todo error - hotovo
        // todo zmenit state

    }

    override fun setTextChanged(text: String) {
        data.set.name = text
        _addCardScreenUIState.update {
            AddCardScreenUIState.ScreenDataChanged(data)
        }
        // todo zapsat value
        // todo state
    }

    override fun deleteSet() {
        viewModelScope.launch {
            val numberOfDeleted = repository.delete(data.set)
            if (numberOfDeleted > 0) {
                _addCardScreenUIState.update {
                    AddCardScreenUIState.SetDeleted()
                }
            } else {

            }
        }
    }

    fun loadSet(id: Long?){
        if (id != null){
            viewModelScope.launch {
                data.set = repository.getSet(id)
                _addCardScreenUIState.update {
                    AddCardScreenUIState.ScreenDataChanged(data)
                }
            }

        }

    }

}