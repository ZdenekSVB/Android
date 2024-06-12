package cz.mendelu.pef.xsvobo.projekt.ui.screens.playSet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.card.ILocalCardsRepository
import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.ui.screens.addCard.AddCardScreenUIState
import cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList.CardListScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaySetScreenViewModel @Inject constructor(
    private val repositoryCards: ILocalCardsRepository
) : ViewModel(), PlaySetScreenActions {


    var cards: MutableList<Card> = mutableListOf()

    private var cardData: PlaySetScreenData = PlaySetScreenData()

    private val _playSetScreenUIState: MutableStateFlow<PlaySetScreenUIState> =
        MutableStateFlow(value = PlaySetScreenUIState.Loading())

    val playSetUIState = _playSetScreenUIState.asStateFlow()

    override fun cardAnswerChanged(text: String) {
        cardData.card.answer = text
        _playSetScreenUIState.update {
            PlaySetScreenUIState.CardAnswerChanged(cardData)
        }
    }

    override fun loadSet(id: Long) {
        viewModelScope.launch {
            repositoryCards.getCardsBySetId(id).collect {cards= it.toMutableList()

            }
        }
    }

    override fun nextCard() {
        TODO("Not yet implemented")
    }


}
