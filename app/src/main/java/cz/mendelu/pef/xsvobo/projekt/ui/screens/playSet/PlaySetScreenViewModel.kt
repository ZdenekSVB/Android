package cz.mendelu.pef.xsvobo.projekt.ui.screens.playSet


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.card.ILocalCardsRepository
import cz.mendelu.pef.xsvobo.projekt.database.set.ILocalSetsRepository
import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaySetScreenViewModel @Inject constructor(
    private val repositoryCards: ILocalCardsRepository,
    private val repositorySets: ILocalSetsRepository
) : ViewModel(), PlaySetScreenActions {

    private var setData: SetListScreenData = SetListScreenData()

    var cards: MutableList<Card> = mutableListOf()

    private var cardData: PlaySetScreenData = PlaySetScreenData()

    private val _playSetScreenUIState: MutableStateFlow<PlaySetScreenUIState> =
        MutableStateFlow(PlaySetScreenUIState.Loading())

    val playSetUIState = _playSetScreenUIState.asStateFlow()

    override fun cardAnswerChanged(text: String) {
        cardData.card.answer = text
        _playSetScreenUIState.update {
            PlaySetScreenUIState.CardAnswerChanged(cardData)
        }
    }

    override fun loadSet(id: Long) {
        viewModelScope.launch {
            setData.set = repositorySets.getSet(id)
            _playSetScreenUIState.value = PlaySetScreenUIState.LoadSet(setData)

        }
        viewModelScope.launch {
            repositoryCards.getCardsBySetId(id).collect { cardsList ->
                cards = cardsList.toMutableList()
                if (cards.isNotEmpty()) {
                    cardData.card = cards[cardData.index]
                }
                _playSetScreenUIState.value = PlaySetScreenUIState.Success(cards)
            }
        }
    }

    override fun nextCard() {
        if (cardData.index < cards.size - 1) {
            cardData.index += 1
            cardData.card = cards[cardData.index]
            _playSetScreenUIState.update {
                PlaySetScreenUIState.Nextcard(cardData)
            }
        } else {
            _playSetScreenUIState.update {
                PlaySetScreenUIState.Nextcard(cardData)
            }
        }
    }

    override fun incrementCorrectCount() {
        cardData.correctCount++
        _playSetScreenUIState.update {
            PlaySetScreenUIState.Nextcard(cardData)
        }
    }
}
