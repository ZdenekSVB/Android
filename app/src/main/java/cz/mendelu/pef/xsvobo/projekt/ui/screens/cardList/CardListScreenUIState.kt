package cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList

import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenData
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenUIState

sealed class CardListScreenUIState {
    class Loading : CardListScreenUIState()
    class Success(val cards: List<Card>) : CardListScreenUIState()
    class SetNameChanged(val data: SetListScreenData) : CardListScreenUIState()

    class CardDeleted: CardListScreenUIState()
    //class CardListChanged(val data: CardListScreenData) : CardListScreenUIState()
}