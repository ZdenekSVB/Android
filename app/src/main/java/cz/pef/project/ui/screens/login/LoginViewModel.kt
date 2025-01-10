package cz.pef.project.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.project.datastore.getLoginState
import cz.pef.project.datastore.saveLoginState
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

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

    fun login(context: Context, onSuccess: () -> Unit) {
        val isUserNameValid = uiState.userNameError == null && uiState.userName.isNotBlank()
        val isPasswordValid = uiState.passwordError == null && uiState.password.isNotBlank()

        if (isUserNameValid && isPasswordValid) {
            // Simulace přihlášení
            viewModelScope.launch {
                saveLoginState(context, isLoggedIn = true, userName = uiState.userName)
            }
            onSuccess()
        }
    }


    private fun validateUserName(userName: String): String? {
        return if (userName.isBlank()) "User name cannot be empty" else null
    }

    private fun validatePassword(password: String): String? {
        return if (password.isBlank()) "Password cannot be empty" else null
    }

    fun checkLoginState(context: Context, onLoginCheck: (Boolean) -> Unit) {
        viewModelScope.launch {
            getLoginState(context).collect { (isLoggedIn, _) ->
                onLoginCheck(isLoggedIn)
            }
        }
    }

}
