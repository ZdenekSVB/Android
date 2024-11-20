package cz.pef.project.ui.screens.user_settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserSettingsViewModel : ViewModel() {
    private val _uiState = mutableStateOf(UserSettingsUiState())
    val uiState: UserSettingsUiState get() = _uiState.value

    fun showEditDialog() {
        _uiState.value = uiState.copy(isEditDialogVisible = true)
    }

    fun hideEditDialog() {
        _uiState.value = uiState.copy(isEditDialogVisible = false)
    }

    fun updateUserDetails(firstName: String, lastName: String, userName: String, password: String) {
        _uiState.value = uiState.copy(
            firstName = firstName,
            lastName = lastName,
            userName = userName,
            password = password
        )
    }
}
