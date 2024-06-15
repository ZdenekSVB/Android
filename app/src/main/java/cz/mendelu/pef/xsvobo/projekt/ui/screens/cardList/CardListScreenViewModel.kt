package cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList

import android.util.Log
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
class CardListScreenViewModel @Inject constructor(
    private val repositoryCards: ILocalCardsRepository,
    private val repositorySets: ILocalSetsRepository
) : ViewModel(), CardListScreenActions {

    var cards: MutableList<Card> = mutableListOf()
    private var cardData: CardListScreenData = CardListScreenData()
    private var setData: SetListScreenData = SetListScreenData()

    private val _cardListScreenUIState: MutableStateFlow<CardListScreenUIState> = MutableStateFlow(CardListScreenUIState.Loading())
    val cardListScreenUIState = _cardListScreenUIState.asStateFlow()

    override fun addCard(id: Long) {
        viewModelScope.launch {
            Log.d("CardViewModel", "addCard called")
            if (cardData.card.id == null) {
                cardData.card.setsId = id
                cardData.card.name = "Card"
                Log.d("CardViewModel", "Inserting card: ${cardData.card.setsId} + ${cardData.card.id}")

                cardData.card.let { repositoryCards.insert(it) }

                //setData.set=repositorySets.getSet(id)
                setData.set.cardsCount += 1
                repositorySets.update(setData.set)

                _cardListScreenUIState.update {
                    CardListScreenUIState.Success(cards)  // Update the state to trigger recomposition
                }
            }
        }
    }

    override fun setTextChanged(text: String) {
        setData.set.name = text
        _cardListScreenUIState.update {
            CardListScreenUIState.SetNameChanged(setData)
        }
        viewModelScope.launch {
            _cardListScreenUIState.update {
                CardListScreenUIState.Success(cards)
            }
        }
    }

    override fun saveSetName() {
        if (setData.set.name.isNotEmpty()) {
            viewModelScope.launch {
                if (setData.set.id != null) {
                    repositorySets.update(setData.set)
                } else {
                    throw Exception("ID NULL")
                }
            }
        } else {
            throw Exception("Cannot Be Empty")
        }
    }

    fun loadSet(id: Long) {
        Log.d("loadSet", "ID: $id")
        viewModelScope.launch {
            setData.set = repositorySets.getSet(id)
            _cardListScreenUIState.update {
                CardListScreenUIState.SetNameChanged(setData)
            }
        }
        viewModelScope.launch {
            repositoryCards.getCardsBySetId(id).collect {
                cards = it.toMutableList()
                _cardListScreenUIState.update {
                    CardListScreenUIState.Success(cards)
                }
            }
        }
    }

    override fun deleteCard(id: Long) {
        viewModelScope.launch {
            val numberOfDeleted = repositoryCards.delete(repositoryCards.getCard(id))
            if (numberOfDeleted > 0) {
                _cardListScreenUIState.update {
                    CardListScreenUIState.CardDeleted()
                }
            }
        }
    }
}
