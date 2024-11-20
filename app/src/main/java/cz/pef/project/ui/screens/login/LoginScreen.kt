package cz.pef.project.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.screens.registration.RegistrationViewModel

@Composable
fun LoginScreen(navigation: INavigationRouter) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState = viewModel.uiState
    val darkTheme = true // Nastavení dark mode

    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Nadpis
                    Text(
                        text = "AI Garden Helper",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // User Name
                    LoginField(
                        label = "User Name",
                        value = uiState.userName,
                        error = uiState.userNameError,
                        onValueChange = { viewModel.updateUserName(it) },
                        onClear = { viewModel.clearUserName() }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Password
                    LoginField(
                        label = "Password",
                        value = uiState.password,
                        error = uiState.passwordError,
                        onValueChange = { viewModel.updatePassword(it) },
                        onClear = { viewModel.clearPassword() },
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tlačítko pro login
                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Login")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // TextButton pro registraci
                    TextButton(onClick = { /* Navigate to Registration */ }) {
                        Text("Don't have an account? Registration")
                    }
                }
            }
        )
    }
}

@Composable
fun LoginField(
    label: String,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    isPassword: Boolean = false
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = error != null,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear text")
                }
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
