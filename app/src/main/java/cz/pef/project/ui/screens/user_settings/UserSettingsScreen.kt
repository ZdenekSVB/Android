package cz.pef.project.ui.screens.user_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.screens.flower_description.FlowerDescriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(navigation: INavigationRouter) {
    val viewModel: UserSettingsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val darkTheme = true // Nastavení dark mode

    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AI Garden Helper") },
                    navigationIcon = {
                        IconButton(onClick = { navigation.returnBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = if (uiState.isLoggedIn) "Logged In" else "Logged Out")

                    Spacer(modifier = Modifier.height(16.dp))

                    UserDetailRow(label = "First Name", value = uiState.firstName)
                    UserDetailRow(label = "Last Name", value = uiState.lastName)
                    UserDetailRow(label = "User Name", value = uiState.userName)
                    UserDetailRow(label = "Password", value = "********") // Skrýt heslo

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = { viewModel.showEditDialog() }) {
                        Text("Edit")
                    }

                }

                if (uiState.isEditDialogVisible) {
                    EditUserDialog(
                        firstName = uiState.firstName,
                        lastName = uiState.lastName,
                        userName = uiState.userName,
                        password = uiState.password,
                        onDismiss = { viewModel.hideEditDialog() },
                        onSave = { firstName, lastName, userName, password ->
                            viewModel.validateAndSaveUserDetails(firstName, lastName, userName, password)
                        },
                        firstNameError = uiState.firstNameError,
                        lastNameError = uiState.lastNameError,
                        userNameError = uiState.userNameError,
                        passwordError = uiState.passwordError
                    )
                }


            }
        )
    }
}

@Composable
fun UserDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
@Composable
fun EditUserDialog(
    firstName: String,
    lastName: String,
    userName: String,
    password: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit,
    firstNameError: String?,
    lastNameError: String?,
    userNameError: String?,
    passwordError: String?
) {
    var newFirstName by remember { mutableStateOf(firstName) }
    var newLastName by remember { mutableStateOf(lastName) }
    var newUserName by remember { mutableStateOf(userName) }
    var newPassword by remember { mutableStateOf(password) }
    var passwordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit User Details") },
        text = {
            Column {
                OutlinedTextField(
                    value = newFirstName,
                    onValueChange = { newFirstName = it },
                    label = { Text("First Name") },
                    isError = firstNameError != null
                )
                if (firstNameError != null) {
                    Text(firstNameError, color = MaterialTheme.colorScheme.error)
                }
                OutlinedTextField(
                    value = newLastName,
                    onValueChange = { newLastName = it },
                    label = { Text("Last Name") },
                    isError = lastNameError != null
                )
                if (lastNameError != null) {
                    Text(lastNameError, color = MaterialTheme.colorScheme.error)
                }
                OutlinedTextField(
                    value = newUserName,
                    onValueChange = { newUserName = it },
                    label = { Text("User Name") },
                    isError = userNameError != null
                )
                if (userNameError != null) {
                    Text(userNameError, color = MaterialTheme.colorScheme.error)
                }
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Password") },
                    isError = passwordError != null,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Clear else Icons.Default.AccountCircle,
                                contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                            )
                        }
                    }
                )
                if (passwordError != null) {
                    Text(passwordError, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(newFirstName, newLastName, newUserName, newPassword)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
