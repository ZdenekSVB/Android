package cz.pef.project.ui.screens.flower_ai

import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
class FlowerAiScreenUITest {

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
    private fun launchFlowerAiScreen() {
        composeRule.activity.setContent {
            navController = rememberNavController()
            val navigationRouter = NavigationRouterImpl(navController)
            MaterialTheme {
                NavGraph(
                    navController = navController as NavHostController,
                    navigation = navigationRouter,
                    startDestination = Destination.FlowerAiScreen.route
                )
            }
        }
    }

    @Test
    fun testFlowerAiScreenElementsExist() {
        // Launch the screen
        launchFlowerAiScreen()

        // Check if the main content is displayed
        composeRule.onNodeWithTag("FlowerAiScreenContent").assertExists()

        // Check if the image selection box is displayed
        composeRule.onNodeWithTag("ImageSelectBox").assertExists()


        // Check if the "Analyze Image" button is displayed
        composeRule.onNodeWithTag("AnalyzeImageButton").assertExists()


    }
}
