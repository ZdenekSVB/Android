package cz.pef.va2_2024_petstore.ui.screens.pet_detail

import java.io.Serializable

sealed class PetDetailScreenUIState () : Serializable {
    object Loading : PetDetailScreenUIState()

}