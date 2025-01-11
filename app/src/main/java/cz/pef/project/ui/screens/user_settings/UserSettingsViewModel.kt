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
    private val userDao: UserDao // Přidání UserDao
) : ViewModel() {

    private val context = application

    private val userPreferencesFlow = DataStoreManager(context).getLoginState()
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
                    DataStoreManager(context).saveLoginState(true, updatedUser.userName)

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

    fun validateAndSaveUserDetails(
        firstName: String,
        lastName: String,
        userName: String,
        password: String
    ) {
        val errors = mutableListOf<String>()
        if (firstName.isBlank()) errors.add("First name cannot be empty")
        if (lastName.isBlank()) errors.add("Last name cannot be empty")
        if (userName.isBlank()) errors.add("User name cannot be empty")
        if (password.isBlank()) errors.add("Password cannot be empty")

        if (errors.isEmpty()) {
            updateUserDetails(firstName, lastName, userName, password)
        } else {
            // Zobrazit chyby v UI
            println("Errors: $errors")
        }
    }

}
