package cz.pef.va2_2024_petstore.ui.screens.pet_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.va2_2024_petstore.R
import cz.pef.va2_2024_petstore.ui.screens.list_of_pets.ListOfPetsScreenError
import cz.pef.va2_2024_petstore.ui.screens.list_of_pets.ListOfPetsScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<PetDetailScreenUIState> = MutableStateFlow(value = PetDetailScreenUIState.Loading)
    val uiState: MutableStateFlow<PetDetailScreenUIState> get() = _uiState


}