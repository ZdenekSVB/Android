package cz.mendelu.pef.xsvobo.projekt.ui.screens.setList

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.card.ILocalCardsRepository
import cz.mendelu.pef.xsvobo.projekt.database.set.ILocalSetsRepository
import cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList.CardListScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetListScreenViewModel @Inject constructor(
    private val repositoryCards: ILocalCardsRepository,
    private val repositorySets: ILocalSetsRepository
) : ViewModel(), SetListScreenActions {

    val setListScreenUIState: MutableState<SetListScreenUIState> = mutableStateOf(
        SetListScreenUIState.Loading()
    )

    private var setData: SetListScreenData = SetListScreenData()

    private val _setListScreenUIState: MutableStateFlow<SetListScreenUIState> =
        MutableStateFlow(value = SetListScreenUIState.Loading())

    override fun createSet() {
        viewModelScope.launch {
            if (setData.set.id == null) {
                setData.set.name = "Set"
                repositorySets.insert(setData.set)
            }
        }
    }

    override fun deleteSet(setId: Long) {
        viewModelScope.launch {
            val numberOfDeleted = repositorySets.delete(repositorySets.getSet(setId))
            if (numberOfDeleted > 0) {
                _setListScreenUIState.update {
                    SetListScreenUIState.SetDeleted()
                }
            }
        }
    }



    fun loadSets() {
        viewModelScope.launch {
            repositorySets.getAll().collect {
                setListScreenUIState.value = SetListScreenUIState.Success(it)
            }
        }
    }


}