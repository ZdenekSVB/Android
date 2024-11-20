package cz.pef.project.ui.screens.user_settings

data class UserSettingsUiState(
    val firstName: String = "John",
    val lastName: String = "Doe",
    val userName: String = "johndoe",
    val password: String = "password",
    val isEditDialogVisible: Boolean = false
)
