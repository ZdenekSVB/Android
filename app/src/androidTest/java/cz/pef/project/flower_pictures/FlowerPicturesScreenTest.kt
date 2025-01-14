package cz.pef.project.flower_pictures

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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class FlowerPicturesScreenTest {

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
        launchFlowerPicturesScreen()

        with(composeRule) {
            // Verify "Add Picture" button is displayed
            onNodeWithContentDescription("Add Picture").assertIsDisplayed()

        }
    }

    @Test
    fun test_add_picture_interaction() {
        launchFlowerPicturesScreen()

        with(composeRule) {
            // Simulate clicking the "Add Picture" button
            onNodeWithContentDescription("Add Picture").performClick()

            // Verify that an action to add a picture has been initiated
            // (In a real test, you may need to verify some mocked behavior)
        }
    }

    @Test
    fun test_picture_list_interaction() {
        launchFlowerPicturesScreen()

        with(composeRule) {
            // Verify picture grid is displayed
            onNodeWithTag("PictureGrid").assertIsDisplayed()


        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun launchFlowerPicturesScreen() {
        composeRule.activity.setContent {
            navController = rememberNavController()
            val navigationRouter = NavigationRouterImpl(navController)
            MaterialTheme {
                NavGraph(
                    navController = navController as NavHostController,
                    navigation = navigationRouter,
                    startDestination = Destination.FlowerPicturesScreen.route
                )
            }
        }
    }
}
