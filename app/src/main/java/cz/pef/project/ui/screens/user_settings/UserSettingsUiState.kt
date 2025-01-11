package cz.pef.project.ui.screens.user_settings

data class UserSettingsUiState(
    val isLoggedIn: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val userName: String = "",
    val password: String = "",
    val isEditDialogVisible: Boolean = false,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val userNameError: String? = null,
    val passwordError: String? = null
)
