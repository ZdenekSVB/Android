package cz.pef.va2_2024_petstore.ui.screens.list_of_pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.va2_2024_petstore.R
import cz.pef.va2_2024_petstore.communication.CommunicationResult
import cz.pef.va2_2024_petstore.communication.IPetsRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListOfPetsViewModel @Inject constructor(private val repository: IPetsRemoteRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<ListOfPetsScreenUIState> = MutableStateFlow(value = ListOfPetsScreenUIState.Loading)
    val uiState: StateFlow<ListOfPetsScreenUIState> get() = _uiState.asStateFlow()

 init {
     viewModelScope.launch {
         val result = withContext(Dispatchers.IO){
             repository.getAllPets("available")
         }

         when(result){
             is CommunicationResult.ConnectionError -> {
                 _uiState.update { ListOfPetsScreenUIState.Error(ListOfPetsScreenError(R.string.no_internet_connection)) }
             }
             is CommunicationResult.Error  -> {

             }
             is CommunicationResult.Exception -> {

             }
             is CommunicationResult.Success -> {
                 _uiState.update { ListOfPetsScreenUIState.DataLoaded(result.data) }
             }

         }

     }

 }

}