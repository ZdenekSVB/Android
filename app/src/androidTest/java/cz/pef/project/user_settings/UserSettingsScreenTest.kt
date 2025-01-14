package cz.pef.project.user_settings

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
import cz.pef.project.ui.screens.user_settings.DarkMode
import cz.pef.project.ui.screens.user_settings.DarkModeButton
import cz.pef.project.ui.screens.user_settings.EditButton
import cz.pef.project.ui.screens.user_settings.FirstNameField
import cz.pef.project.ui.screens.user_settings.LastNameField
import cz.pef.project.ui.screens.user_settings.PasswordField
import cz.pef.project.ui.screens.user_settings.UserNameField
import cz.pef.project.ui.screens.user_settings.UserSettingsScreen
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
class UserSettingsScreenTest {

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
    fun test_is_displayed() {
        launchUserSettingsScreen()

        with(composeRule) {
            // Check if user details are displayed
            onNodeWithTag(FirstNameField).assertIsDisplayed()

            onNodeWithTag(LastNameField).assertIsDisplayed()

            onNodeWithTag(UserNameField).assertIsDisplayed()

            // Hide password test
            onNodeWithTag(PasswordField).assertIsDisplayed()
        }
    }

    @Test
    fun test_toggle_dark_mode() {
        launchUserSettingsScreen()

        with(composeRule) {
            // Toggle dark mode
            onNodeWithTag(DarkModeButton).assertIsDisplayed().performClick()

        }
    }


    @OptIn(ExperimentalFoundationApi::class)
    private fun launchUserSettingsScreen() {
        composeRule.activity.setContent {
            navController = rememberNavController()
            val navigationRouter = NavigationRouterImpl(navController)
            MaterialTheme {
                NavGraph(
                    navController = navController as NavHostController,
                    navigation = navigationRouter,
                    startDestination = Destination.UserSettingsScreen.route
                )
            }
        }
    }
}
