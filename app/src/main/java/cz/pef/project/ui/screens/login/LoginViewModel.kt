package cz.pef.project.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.project.dao.UserDao
import cz.pef.project.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application, private val userDao: UserDao,  // Inject UserDao
    private val datastore: DataStoreManager // Inject DataStoreManager
) : AndroidViewModel(application) {

    private val _uiState = mutableStateOf(LoginUiState())
    val uiState: LoginUiState get() = _uiState.value

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

    fun login(context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val isUserNameValid = uiState.userNameError == null && uiState.userName.isNotBlank()
        val isPasswordValid = uiState.passwordError == null && uiState.password.isNotBlank()

        if (isUserNameValid && isPasswordValid) {
            viewModelScope.launch {
                val user = userDao.getUserByUserName(uiState.userName)
                if (user != null && user.password == uiState.password) {
                    // Save login state
                    datastore.saveLoginState(isLoggedIn = true, userName = user.userName)
                    onSuccess()
                } else {
                    onError("Invalid username or password")
                }
            }
        } else {
            onError("Please fill in all fields correctly")
        }
    }

    private fun validateUserName(userName: String): String? {
        return if (userName.isBlank()) "User name cannot be empty" else null
    }

    private fun validatePassword(password: String): String? {
        return if (password.isBlank()) "Password cannot be empty" else null
    }
}
