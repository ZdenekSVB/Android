package cz.pef.project.screens.login

import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.pef.project.navigation.Destination
import cz.pef.project.navigation.NavGraph
import cz.pef.project.navigation.NavigationRouterImpl
import cz.pef.project.ui.activity.MainActivity
import cz.pef.project.ui.screens.login.LoginButton
import cz.pef.project.ui.screens.login.PasswordLoginField
import cz.pef.project.ui.screens.login.UserNameLoginField
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters



@ExperimentalCoroutinesApi
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LoginScreenTest {

    private lateinit var navController: NavController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_is_displayed_perform_text_input() {
        launchLoginScreen()
        with(composeRule) {
            onNodeWithTag(UserNameLoginField).assertIsDisplayed()
            onNodeWithTag(UserNameLoginField).performTextInput("testuser")

            waitForIdle()
            Thread.sleep(1000) // Just for better visibility during testing
        }
    }

    @Test
    fun test_input_valid_credentials_and_navigate_next() {
        launchLoginScreen()
        with(composeRule) {
            onNodeWithTag(UserNameLoginField).assertIsDisplayed()
            onNodeWithTag(PasswordLoginField).assertIsDisplayed()

            onNodeWithTag(UserNameLoginField).performTextInput("validuser")
            onNodeWithTag(PasswordLoginField).performTextInput("validpassword")

            onNodeWithTag(LoginButton).performClick()
            waitForIdle()

            // Check navigation
            assertEquals(Destination.LoginScreen.route, navController.currentDestination?.route)
        }
    }

    @Test
    fun test_input_invalid_credentials_and_stay_on_login() {
        launchLoginScreen()
        with(composeRule) {
            onNodeWithTag(UserNameLoginField).assertIsDisplayed()
            onNodeWithTag(PasswordLoginField).assertIsDisplayed()

            onNodeWithTag(UserNameLoginField).performTextInput("invaliduser")
            onNodeWithTag(PasswordLoginField).performTextInput("wrongpassword")

            onNodeWithTag(LoginButton).performClick()
            waitForIdle()

            // Ensure the route is still the login screen
            assertEquals(Destination.LoginScreen.route, navController.currentDestination?.route)
        }
    }

    @Test
    fun test_input_invalid_email_and_display_error() {
        launchLoginScreen()
        with(composeRule) {
            onNodeWithTag(UserNameLoginField).assertIsDisplayed()
            onNodeWithTag(PasswordLoginField).assertIsDisplayed()

            onNodeWithTag(UserNameLoginField).performTextInput("invalidemail")
            onNodeWithTag(PasswordLoginField).performTextInput("password123")

            onNodeWithTag(LoginButton).performClick()
            onNodeWithText("Invalid username or password").assertIsDisplayed() // Assuming this error is shown
        }
    }

    @Test
    fun test_input_invalid_password_and_display_error() {
        launchLoginScreen()
        with(composeRule) {
            onNodeWithTag(UserNameLoginField).assertIsDisplayed()
            onNodeWithTag(PasswordLoginField).assertIsDisplayed()

            onNodeWithTag(UserNameLoginField).performTextInput("valid@email.com")
            onNodeWithTag(PasswordLoginField).performTextInput("short")

            onNodeWithTag(LoginButton).performClick()
            onNodeWithText("Invalid username or password").assertIsDisplayed() // Assuming this error is shown
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun launchLoginScreen() {
        composeRule.activity.setContent {
            navController = rememberNavController()
            val navigationRouter = NavigationRouterImpl(navController)
            MaterialTheme {
                NavGraph(
                    navController = navController as NavHostController,
                    navigation = navigationRouter,
                    startDestination = Destination.LoginScreen.route
                )
            }
        }
    }
}
