package cz.pef.mendelu.examtemplate2024.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.mendelu.examtemplate2024.R
import cz.pef.mendelu.examtemplate2024.communication.CommunicationResult
import cz.pef.mendelu.examtemplate2024.communication.Country
import cz.pef.mendelu.examtemplate2024.communication.IUserRemoteRepository
import cz.pef.mendelu.examtemplate2024.communication.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: IUserRemoteRepository
) : ViewModel() {

    private val _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    private val _currentCountry: MutableStateFlow<Country?> = MutableStateFlow(null)
    val currentUser: StateFlow<User?> get() = _currentUser.asStateFlow()
    val currentCountry: StateFlow<Country?> get() = _currentCountry.asStateFlow()

    // Generates a new random user from user_1.json to user_9.json
    fun generateNewRandomUser() {
        val randomUserNumber = Random.nextInt(1, 10)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getUser(randomUserNumber)
            }
            if (result is CommunicationResult.Success) {
                _currentUser.value = result.data
            } else {
                _currentUser.value = null // Handle error
            }
        }
    }

    // Gets the currency for a specific country
    fun getCurrencyForCountry(countryName: String){
        viewModelScope.launch {
        val country = repository.getCountryByName(countryName)
        country?.currencies?.values?.firstOrNull()?.name ?: "Unknown"
        }

    }

    // Gets the capital for a specific country
    fun getCapitalForCountry(countryName: String) {
        viewModelScope.launch {
        val country = repository.getCountryByName(countryName)
        country?.capital?.firstOrNull() ?: "Unknown"
        }
    }
}
