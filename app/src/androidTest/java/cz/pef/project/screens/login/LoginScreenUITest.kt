package cz.pef.project.screens.login

import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cz.pef.project.navigation.Destination
import cz.pef.project.navigation.NavGraph
import cz.pef.project.ui.activity.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class LoginScreenUITest {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_display_all_login_elements() {
        launchLoginScreenWithNavigation()
        with(composeRule) {
            onNodeWithTag("UserNameLoginField").assertIsDisplayed()
            onNodeWithTag("PasswordLoginField").assertIsDisplayed()
            onNodeWithTag("LoginButton").assertIsDisplayed()
        }
    }

    @Test
    fun test_input_valid_credentials_and_navigate_next() {
        launchLoginScreenWithNavigation()
        with(composeRule) {
            onNodeWithTag("UserNameLoginField").performTextInput("validUser")
            onNodeWithTag("PasswordLoginField").performTextInput("validPass123")

            onNodeWithTag("LoginButton").performClick()
            waitForIdle()

            val route = navController.currentBackStackEntry?.destination?.route
            assertTrue(route == Destination.GardenOverviewScreen.route)
        }
    }

    @Test
    fun test_input_invalid_credentials_stay_on_login() {
        launchLoginScreenWithNavigation()
        with(composeRule) {
            onNodeWithTag("UserNameLoginField").performTextInput("invalidUser")
            onNodeWithTag("PasswordLoginField").performTextInput("wrongPass")

            onNodeWithTag("LoginButton").performClick()
            waitForIdle()

            val route = navController.currentBackStackEntry?.destination?.route
            assertTrue(route == Destination.LoginScreen.route)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun launchLoginScreenWithNavigation() {
        composeRule.activity.setContent {
            MaterialTheme {
                navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    startDestination = Destination.LoginScreen.route
                )
            }
        }
    }

}
