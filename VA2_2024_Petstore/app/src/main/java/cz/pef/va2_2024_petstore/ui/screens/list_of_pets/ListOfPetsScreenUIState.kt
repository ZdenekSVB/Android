package cz.pef.va2_2024_petstore.ui.screens.list_of_pets

import cz.pef.va2_2024_petstore.communication.Pet
import java.io.Serializable

sealed class ListOfPetsScreenUIState () : Serializable {
    object Loading : ListOfPetsScreenUIState()
    class DataLoaded (val pets: List<Pet>) : ListOfPetsScreenUIState()
    class Error(val error:ListOfPetsScreenError) : ListOfPetsScreenUIState()

}