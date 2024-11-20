package cz.pef.project.ui.screens.registration

data class RegistrationUiState(
    val firstName: String = "",
    val lastName: String = "",
    val userName: String = "",
    val password: String = "",
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val userNameError: String? = null,
    val passwordError: String? = null
)
