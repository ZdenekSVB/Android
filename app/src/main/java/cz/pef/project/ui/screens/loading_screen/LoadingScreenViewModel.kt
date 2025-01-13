package cz.pef.project.ui.screens.loading_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.project.dao.UserDao
import cz.pef.project.datastore.DataStoreManager
import cz.pef.project.ui.screens.user_settings.UserSettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingScreenViewModel @Inject constructor(
    private val datastore: DataStoreManager
) : ViewModel() {


    private val _uiState = MutableStateFlow(UserSettingsUiState())
    val uiState: StateFlow<UserSettingsUiState> = _uiState

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme
    init {
        // Debounce search query to optimize filtering
        viewModelScope.launch {
            observeThemePreference()
        }
    }

    private fun observeThemePreference() {
        viewModelScope.launch {
            datastore.darkModeFlow.collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
    }
}