package cz.mendelu.pef.xsvobo.projekt.ui.screens.addCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xsvobo.projekt.database.card.ILocalCardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardScreenViewModel @Inject constructor(
    private val repositoryCards: ILocalCardsRepository
) : ViewModel(), AddCardScreenActions {

    private var cardData: AddCardScreenData = AddCardScreenData()

    private val _addCardScreenUIState: MutableStateFlow<AddCardScreenUIState> =
        MutableStateFlow(value = AddCardScreenUIState.Loading)

    val addCardUIState = _addCardScreenUIState.asStateFlow()

    override fun cardQuestionChanged(text: String) {
        cardData.card.question = text
        _addCardScreenUIState.update {
            AddCardScreenUIState.CardDataChanged(cardData)
        }
    }

    override fun cardTextChanged(text: String) {
        cardData.card.name = text
        _addCardScreenUIState.update {
            AddCardScreenUIState.CardDataChanged(cardData)
        }
    }

    override fun cardRightAnswerChanged(text: String) {
        cardData.card.rightAnswer = text
        _addCardScreenUIState.update {
            AddCardScreenUIState.CardDataChanged(cardData)
        }
    }

    override fun saveCard() {
        if (cardData.card.rightAnswer != "" && cardData.card.question != "" && cardData.card.name != "") {
            viewModelScope.launch {
                repositoryCards.update(cardData.card)
                _addCardScreenUIState.update {
                    AddCardScreenUIState.CardSaved
                }
            }
        } else {
            cardData.cardTextError = "Cannot be empty"
            _addCardScreenUIState.update {
                AddCardScreenUIState.CardDataChanged(cardData)
            }
        }

    }

    override fun loadCard(id: Long?) {
        if (id != null) {
            viewModelScope.launch {
                cardData.card = repositoryCards.getCard(id)
                _addCardScreenUIState.update {
                    AddCardScreenUIState.CardDataChanged(cardData)
                }
            }

        }

    }

}
