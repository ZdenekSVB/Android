package cz.mendelu.pef.xsvobo.projekt.ui.screens

import cz.mendelu.pef.xsvobo.projekt.model.Set

sealed class SetListScreenUIState {
    class Loading : SetListScreenUIState()
    class Success(val sets: List<Set>) : SetListScreenUIState()
    class SetDeleted: SetListScreenUIState()
}