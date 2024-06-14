package cz.mendelu.pef.xsvobo.projekt.ui.screens.playSet

import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList.CardListScreenUIState

sealed class PlaySetScreenUIState {
    class Loading : PlaySetScreenUIState()
    class CardAnswerChanged(val data: PlaySetScreenData) : PlaySetScreenUIState()
    class Success(val cards: List<Card>) : PlaySetScreenUIState()
    class Nextcard(val data: PlaySetScreenData) : PlaySetScreenUIState()

}