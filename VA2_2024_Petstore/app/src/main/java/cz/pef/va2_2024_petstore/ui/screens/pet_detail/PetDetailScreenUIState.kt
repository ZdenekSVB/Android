package cz.pef.va2_2024_petstore.ui.screens.pet_detail

import cz.pef.va2_2024_petstore.communication.Pet
import java.io.Serializable

sealed class PetDetailScreenUIState () : Serializable {
    object Loading : PetDetailScreenUIState()
    class DataLoaded (val pet: Pet) : PetDetailScreenUIState()
    class Error(val error: PetDetailScreenError) : PetDetailScreenUIState()

}