package cz.mendelu.pef.xsvobo.projekt.ui.screens.addCard


sealed class AddCardScreenUIState {
    data object Loading : AddCardScreenUIState()
    class CardDataChanged(val data: AddCardScreenData) : AddCardScreenUIState()
    data object CardSaved : AddCardScreenUIState()

}