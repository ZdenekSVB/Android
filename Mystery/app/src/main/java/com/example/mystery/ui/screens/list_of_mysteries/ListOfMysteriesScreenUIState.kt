package com.example.mystery.ui.screens.list_of_pets

import com.example.mystery.communication.MysteryItem
import java.io.Serializable

sealed class ListOfMysteriesScreenUIState () : Serializable {
    object Loading : ListOfMysteriesScreenUIState()
    class DataLoaded(val mysteries: MysteryItem) : ListOfMysteriesScreenUIState()
    class Error(val error:ListOfMysteriesScreenError) : ListOfMysteriesScreenUIState()

}