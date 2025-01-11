package cz.pef.project.ui.screens.user_settings

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.project.dao.UserDao
import cz.pef.project.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow


@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    application: Application,
    private val datastore: DataStoreManager, // Inject DataStoreManager
    private val userDao: UserDao // Přidání UserDao
) : ViewModel() {

    private val context = application

    private val userPreferencesFlow = datastore.getLoginState()
    private val _uiState = MutableStateFlow(UserSettingsUiState())
    val uiState: StateFlow<UserSettingsUiState> = _uiState
    init {
        viewModelScope.launch {
            userPreferencesFlow.collect { (isLoggedIn, userName) ->
                val user = userName?.let { userDao.getUserByUserName(it) }
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = isLoggedIn,
                    userName = user?.userName ?: "Unknown",
                    firstName = user?.firstName ?: "",
                    lastName = user?.lastName ?: "",
                    password = user?.password ?: ""
                )
            }
        }
    }


    fun showEditDialog() {
        _uiState.value = _uiState.value.copy(isEditDialogVisible = true)
    }

    fun hideEditDialog() {
        _uiState.value = _uiState.value.copy(isEditDialogVisible = false)
    }

    fun updateUserDetails(firstName: String, lastName: String, userName: String, password: String) {
        val currentUser = uiState.value.userName
        if (currentUser != null) {
            viewModelScope.launch {
                val user = userDao.getUserByUserName(currentUser)
                if (user != null) {
                    val updatedUser = user.copy(
                        firstName = firstName,
                        lastName = lastName,
                        userName = userName,
                        password = password
                    )
                    userDao.updateUser(updatedUser)

                    // Aktualizace DataStore
                    datastore.saveLoginState(true, updatedUser.userName)

                    _uiState.value = _uiState.value.copy(
                        firstName = firstName,
                        lastName = lastName,
                        userName = userName,
                        password = password,
                        isEditDialogVisible = false
                    )
                }
            }
        }
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

    fun validateAndSaveUserDetails(
        firstName: String,
        lastName: String,
        userName: String,
        password: String
    ) {
        val firstNameError = validateFirstName(firstName)
        val lastNameError = validateLastName(lastName)
        val userNameError = validateUserName(userName)
        val passwordError = validatePassword(password)

        if (firstNameError == null && lastNameError == null && userNameError == null && passwordError == null) {
            updateUserDetails(firstName, lastName, userName, password)
        } else {
            // Nastavit chyby do UI state
            _uiState.value = _uiState.value.copy(
                firstNameError = firstNameError,
                lastNameError = lastNameError,
                userNameError = userNameError,
                passwordError = passwordError
            )
        }
    }

}
