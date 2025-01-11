package cz.pef.project.ui.screens.registration

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.project.DB.UserEntity
import cz.pef.project.dao.UserDao
import cz.pef.project.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userDao: UserDao,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private var _uiState = mutableStateOf(RegistrationUiState())
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

    fun register(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (validateInputs()) {
                val existingUser = userDao.getUserByUserName(uiState.userName)
                if (existingUser != null) {
                    _uiState.value = uiState.copy(userNameError = "User name already exists")
                } else {
                    val newUser = UserEntity(
                        firstName = uiState.firstName,
                        lastName = uiState.lastName,
                        userName = uiState.userName,
                        password = uiState.password
                    )
                    userDao.insertUser(newUser)

                    // Save login state to DataStore
                    dataStoreManager.saveLoginState(isLoggedIn = true, userName = uiState.userName)

                    onSuccess()
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        return uiState.firstNameError == null &&
                uiState.lastNameError == null &&
                uiState.userNameError == null &&
                uiState.passwordError == null
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