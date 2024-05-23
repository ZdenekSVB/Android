package cz.mendelu.pef.xsvobo.projekt.ui.screens

sealed class AddCardScreenUIState {
    class Loading : AddCardScreenUIState()
    class SetSaved : AddCardScreenUIState()
    class SetDeleted: AddCardScreenUIState()
    class ScreenDataChanged(val data: AddCardScreenData) : AddCardScreenUIState()
}