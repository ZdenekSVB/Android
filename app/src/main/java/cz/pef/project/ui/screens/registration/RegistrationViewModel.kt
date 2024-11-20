package cz.pef.project.ui.screens.registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {
    private val _uiState = mutableStateOf(RegistrationUiState())
    val uiState: RegistrationUiState get() = _uiState.value

    fun updateFirstName(value: String) {
        _uiState.value = uiState.copy(firstName = value, firstNameError = validateFirstName(value))
    }

    fun clearFirstName() {
        _uiState.value = uiState.copy(firstName = "")
    }

    fun updateLastName(value: String) {
        _uiState.value = uiState.copy(lastName = value, lastNameError = validateLastName(value))
    }

    fun clearLastName() {
        _uiState.value = uiState.copy(lastName = "")
    }

    fun updateUserName(value: String) {
        _uiState.value = uiState.copy(userName = value, userNameError = validateUserName(value))
    }

    fun clearUserName() {
        _uiState.value = uiState.copy(userName = "")
    }

    fun updatePassword(value: String) {
        _uiState.value = uiState.copy(password = value, passwordError = validatePassword(value))
    }

    fun clearPassword() {
        _uiState.value = uiState.copy(password = "")
    }

    fun register() {
        // Logika pro registraci
    }

    private fun validateFirstName(name: String): String? {
        return if (name.isBlank()) "First name cannot be empty" else null
    }

    private fun validateLastName(name: String): String? {
        return if (name.isBlank()) "Last name cannot be empty" else null
    }

    private fun validateUserName(userName: String): String? {
        return if (userName.length < 5) "User name must be at least 5 characters" else null
    }

    private fun validatePassword(password: String): String? {
        return if (password.length < 8) "Password must be at least 8 characters" else null
    }
}
