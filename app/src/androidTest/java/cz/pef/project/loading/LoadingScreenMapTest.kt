package cz.pef.project.loading

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
import cz.pef.project.ui.activity.MainActivity
import cz.pef.project.navigation.NavigationRouterImpl
import cz.pef.project.ui.screens.loading_screen.LoadingScreenMap
import cz.pef.project.ui.theme.basicMargin
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class LoadingScreenMapTest {

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
    fun test_loading_screen_displayed() {
        launchLoadingScreen()

        with(composeRule) {
            // Check if loading screen is displayed
            onNodeWithTag("LoadingScreen").assertIsDisplayed()

            // Check if CircularProgressIndicator is displayed
            onNodeWithTag("LoadingProgressIndicator").assertIsDisplayed()
        }
    }

    @Test
    fun test_loading_screen_progress_indicator() {
        launchLoadingScreen()

        with(composeRule) {
            // Verify that the CircularProgressIndicator is displayed and working
            onNodeWithTag("LoadingProgressIndicator").assertExists()
        }
    }

    @Test
    fun test_navigation_to_flower_location_screen() {
        launchLoadingScreen()

        with(composeRule) {
            assertEquals(Destination.FlowerLocationScreen.route, navController.currentDestination?.route)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun launchLoadingScreen() {
        composeRule.activity.setContent {
            navController = rememberNavController()
            val navigationRouter = NavigationRouterImpl(navController)
            MaterialTheme {
                NavGraph(
                    navController = navController as NavHostController,
                    navigation = navigationRouter,
                    startDestination = Destination.LoadingScreenMap.route
                )
            }
        }
    }
}
