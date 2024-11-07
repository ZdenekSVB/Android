package cz.pef.mendelu.examtemplate2024.ui.screens

import cz.pef.mendelu.examtemplate2024.communication.User
import java.io.Serializable

sealed class MainScreenUIState () : Serializable {
    object Loading : MainScreenUIState()
    class DataLoaded(val user: User) : MainScreenUIState()
    class DataDeleted() : MainScreenUIState()
    class Error(val error: MainScreenError) : MainScreenUIState()
}