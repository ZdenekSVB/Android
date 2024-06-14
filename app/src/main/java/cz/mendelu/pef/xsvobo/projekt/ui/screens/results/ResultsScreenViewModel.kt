package cz.mendelu.pef.xsvobo.projekt.ui.screens.results


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.card.ILocalCardsRepository
import cz.mendelu.pef.xsvobo.projekt.database.set.ILocalSetsRepository
import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList.CardListScreenData
import cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList.CardListScreenUIState
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsScreenViewModel @Inject constructor(
    private val repositoryCards: ILocalCardsRepository,
    private val repositorySets: ILocalSetsRepository
) : ViewModel(), ResultsScreenActions {


    private var cardData: CardListScreenData = CardListScreenData()

    private var setData: SetListScreenData = SetListScreenData()

    var cards: MutableList<Card> = mutableListOf()


    private val _resultsScreenUIState: MutableStateFlow<ResultsScreenUIState> =
        MutableStateFlow(ResultsScreenUIState.Loading())

    val resultsScreenUIState = _resultsScreenUIState.asStateFlow()

    override fun loadSet(id: Long) {
        increaseLatest(id)
        viewModelScope.launch {
            repositoryCards.getCardsBySetId(id).collect { cardsList ->
                cards = cardsList.toMutableList()
                _resultsScreenUIState.value = ResultsScreenUIState.Success(cards)
            }
        }
    }

    override fun increaseLatest(id: Long) {

        viewModelScope.launch {
            setData.set = repositorySets.getSet(id)
            setData.set.latest += 1
            repositorySets.update(setData.set)
            Log.d("GetSet", "Latest: " + setData.set.latest)
        }
    }
}
