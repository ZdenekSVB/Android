package cz.pef.project.registration

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
import cz.pef.project.ui.screens.registration.*
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
class RegistrationScreenTest {

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
        launchRegistrationScreen()
        with(composeRule) {
            onNodeWithTag(FirstNameField).assertIsDisplayed()
            onNodeWithTag(FirstNameField).performTextInput("John")

            onNodeWithTag(LastNameField).assertIsDisplayed()
            onNodeWithTag(LastNameField).performTextInput("Doe")

            waitForIdle()
        }
    }

    @Test
    fun test_input_valid_credentials_and_navigate_next() {
        launchRegistrationScreen()
        with(composeRule) {
            onNodeWithTag(FirstNameField).performTextInput("John")
            onNodeWithTag(LastNameField).performTextInput("Doe")
            onNodeWithTag(UserNameRegistrationField).performTextInput("johndoe")
            onNodeWithTag(PasswordRegistrationField).performTextInput("securepassword")

            onNodeWithTag(RegistrationButtonTag).performClick()
            waitForIdle()

            // Check navigation
            assertEquals(Destination.GardenOverviewScreen.route, navController.currentDestination?.route)
        }
    }

    @Test
    fun test_input_invalid_password_and_display_error() {
        launchRegistrationScreen()
        with(composeRule) {
            onNodeWithTag(PasswordRegistrationField).performTextInput("123")
            onNodeWithTag(RegistrationButtonTag).performClick()

            // Verify error message is shown
            onNodeWithText("Password must be at least 8 characters").assertIsDisplayed()
        }
    }

    @Test
    fun test_navigation_to_login_screen() {
        launchRegistrationScreen()
        with(composeRule) {
            onNodeWithText("Already have an account? Login").performClick()
            waitForIdle()

            // Ensure navigation to LoginScreen
            assertEquals(Destination.LoginScreen.route, navController.currentDestination?.route)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun launchRegistrationScreen() {
        composeRule.activity.setContent {
            navController = rememberNavController()
            val navigationRouter = NavigationRouterImpl(navController)
            MaterialTheme {
                NavGraph(
                    navController = navController as NavHostController,
                    navigation = navigationRouter,
                    startDestination = Destination.RegistrationScreen.route
                )
            }
        }
    }

}
