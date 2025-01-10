package cz.pef.project.ui.screens.user_settings

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.project.datastore.getLoginState
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
    application: Application
) : ViewModel() {

    private val userPreferencesFlow = getLoginState(application)
    private val _uiState = MutableStateFlow(UserSettingsUiState())
    val uiState: StateFlow<UserSettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            userPreferencesFlow.collect { (isLoggedIn, userName) ->
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = isLoggedIn,
                    userName = userName ?: "Unknown"
                )
            }
        }
    }

    fun showEditDialog() {
        println("Edit dialog shown!") // Debug log
        _uiState.value = _uiState.value.copy(isEditDialogVisible = true)
    }

    fun hideEditDialog() {
        _uiState.value = _uiState.value.copy(isEditDialogVisible = false)
    }

    fun updateUserDetails(firstName: String, lastName: String, userName: String, password: String) {
        _uiState.value = _uiState.value.copy(
            firstName = firstName,
            lastName = lastName,
            userName = userName,
            password = password
        )
    }
}
