package cz.pef.va2_2024_petstore.ui.screens.pet_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.va2_2024_petstore.R
import cz.pef.va2_2024_petstore.communication.CommunicationResult
import cz.pef.va2_2024_petstore.communication.IPetsRemoteRepository
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
class PetDetailViewModel @Inject constructor(private val repository: IPetsRemoteRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<PetDetailScreenUIState> = MutableStateFlow(value = PetDetailScreenUIState.Loading)
    val uiState: MutableStateFlow<PetDetailScreenUIState> get() = _uiState

    fun loadDetail(id:Long){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                repository.getPetById(petId = id)
            }

            when(result){
                is CommunicationResult.ConnectionError -> {}
                is CommunicationResult.Error  -> {}
                is CommunicationResult.Exception -> {}
                is CommunicationResult.Success -> {
                    _uiState.update { PetDetailScreenUIState.DataLoaded(result.data) }
                }

            }

        }

    }



}