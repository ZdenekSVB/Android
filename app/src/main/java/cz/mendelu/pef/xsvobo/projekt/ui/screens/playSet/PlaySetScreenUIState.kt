package cz.mendelu.pef.xsvobo.projekt.ui.screens.playSet

sealed class PlaySetScreenUIState {
    class Loading : PlaySetScreenUIState()
    class CardAnswerChanged(val data: PlaySetScreenData) : PlaySetScreenUIState()
    class Nextcard() : PlaySetScreenUIState()

}