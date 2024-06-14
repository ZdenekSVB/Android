package cz.mendelu.pef.xsvobo.projekt.ui.screens.menu

import cz.mendelu.pef.xsvobo.projekt.model.Set

sealed class MenuScreenUIState {
    class Loading : MenuScreenUIState()
    class Success(val sets: List<Set>) : MenuScreenUIState()
}