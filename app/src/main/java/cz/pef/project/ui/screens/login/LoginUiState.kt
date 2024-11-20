package cz.pef.project.ui.screens.login

data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val userNameError: String? = null,
    val passwordError: String? = null
)
