package cz.mendelu.pef.xsvobo.projekt.ui.screens.addCard

import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList.CardListScreenData
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenData

sealed class AddCardScreenUIState {
    class Loading : AddCardScreenUIState()
    class CardDataChanged(val data: AddCardScreenData) : AddCardScreenUIState()
    class CardSaved() : AddCardScreenUIState()

}