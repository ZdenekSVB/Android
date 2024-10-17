package com.example.mystery.ui.screens.list_of_pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystery.R
import com.example.mystery.communication.CommunicationResult
import com.example.mystery.communication.IMysteryRemoteRepository
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
class ListOfMysteriesViewModel @Inject constructor(private val repository: IMysteryRemoteRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<ListOfMysteriesScreenUIState> = MutableStateFlow(value = ListOfMysteriesScreenUIState.Loading)
    val uiState: StateFlow<ListOfMysteriesScreenUIState> get() = _uiState.asStateFlow()

 init {
     viewModelScope.launch {
         val result = withContext(Dispatchers.IO){
             repository.getMystery()
         }

         when(result){
             is CommunicationResult.ConnectionError -> {
                 _uiState.update { ListOfMysteriesScreenUIState.Error(ListOfMysteriesScreenError(R.string.no_internet_connection)) }
             }
             is CommunicationResult.Error  -> {

             }
             is CommunicationResult.Exception -> {

             }
             is CommunicationResult.Success -> {
                 _uiState.update { ListOfMysteriesScreenUIState.DataLoaded(result.data) }
             }

         }

     }

 }

}