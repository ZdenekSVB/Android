package cz.pef.project.ui.screens.login

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.pef.project.navigation.INavigationRouter
import androidx.compose.ui.res.stringResource
import cz.pef.project.R

@Composable
fun LoginScreen(navigation: INavigationRouter) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState = viewModel.uiState
    val darkTheme = true // Set dark mode

    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(), content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = stringResource(id = R.string.ai_garden_helper),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                // User Name
                LoginField(label = stringResource(id = R.string.user_name),
                    value = uiState.userName,
                    error = uiState.userNameError,
                    onValueChange = { viewModel.updateUserName(it) },
                    testTag = "UserNameLoginField",
                    onClear = { viewModel.clearUserName() })

                Spacer(modifier = Modifier.height(8.dp))

                // Password
                LoginField(
                    label = stringResource(id = R.string.password),
                    value = uiState.password,
                    error = uiState.passwordError,
                    onValueChange = { viewModel.updatePassword(it) },
                    onClear = { viewModel.clearPassword() },
                    isPassword = true,
                    testTag = "PasswordLoginField"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Login Button
                LoginButton(
                    viewModel = viewModel, navigation = navigation, testTag = "LoginButton"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Registration Button
                TextButton(onClick = { navigation.navigateToRegistration() }) {
                    Text(stringResource(id = R.string.dont_have_account_registration))
                }
            }
        })
    }
}

@Composable
fun LoginField(
    label: String,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    isPassword: Boolean = false,
    testTag: String
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = error != null,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag), // Přidání testovacího tagu
            trailingIcon = {
                IconButton(onClick = onClear) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = stringResource(id = R.string.clear_text)
                    )
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

@Composable
fun LoginButton(
    viewModel: LoginViewModel,
    navigation: INavigationRouter,
    testTag: String
) {
    val context = LocalContext.current
    Button(
        onClick = {
            viewModel.login(context = context,
                onSuccess = { navigation.navigateToGardenOverviewScreen() },
                onError = { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                })
        },
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag) // Přidání testovacího tagu
    ) {
        Text(stringResource(id = R.string.login))
    }
}
