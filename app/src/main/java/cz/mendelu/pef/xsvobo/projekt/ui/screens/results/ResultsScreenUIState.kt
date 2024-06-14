package cz.mendelu.pef.xsvobo.projekt.ui.screens.results

import cz.mendelu.pef.xsvobo.projekt.model.Card

sealed class ResultsScreenUIState {
    class Loading : ResultsScreenUIState()
    class Success(val cards: List<Card>) : ResultsScreenUIState()

}