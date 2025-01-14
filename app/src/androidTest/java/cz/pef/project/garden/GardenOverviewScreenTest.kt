package cz.pef.project.garden

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
import cz.pef.project.ui.screens.garden_overview.GardenOverviewScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class GardenOverviewScreenTest {

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
    fun test_screen_elements_displayed() {
        launchGardenOverviewScreen()

        with(composeRule) {

            // Test if search field is visible
            onNodeWithTag("SearchField").assertIsDisplayed()

            // Test if plant list is visible
            onNodeWithTag("PlantList").assertIsDisplayed()
        }
    }



    @OptIn(ExperimentalFoundationApi::class)
    private fun launchGardenOverviewScreen() {
        composeRule.activity.setContent {
            navController = rememberNavController()
            val navigationRouter = NavigationRouterImpl(navController)
            MaterialTheme {
                NavGraph(
                    navController = navController as NavHostController,
                    navigation = navigationRouter,
                    startDestination = Destination.GardenOverviewScreen.route
                )
            }
        }
    }
}
