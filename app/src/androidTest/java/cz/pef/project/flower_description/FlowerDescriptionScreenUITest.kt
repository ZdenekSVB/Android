package cz.pef.project.flower_description

import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.pef.project.R
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
class FlowerDescriptionScreenUITest {

    private lateinit var navController: NavController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun launchFlowerDescriptionScreen() {
        composeRule.activity.setContent {
            navController = rememberNavController()
            val navigationRouter = NavigationRouterImpl(navController)
            MaterialTheme {
                NavGraph(
                    navController = navController as NavHostController,
                    navigation = navigationRouter,
                    startDestination = Destination.FlowerDescriptionScreen.route
                )
            }
        }
    }

    @Test
    fun testFlowerDescriptionScreenElementsExist() {
        // Launch the screen
        launchFlowerDescriptionScreen()

        // Check if the name of the plant is displayed
        composeRule.onNodeWithTag("EditNameButton").assertExists() // Check for the edit button

        // Check if the description text is displayed
        composeRule.onNodeWithTag("DescriptionText").assertExists()

    }
}
