package cz.pef.mendelu.examtemplate2024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import cz.pef.mendelu.examtemplate2024.navigation.Destination
import cz.pef.mendelu.examtemplate2024.navigation.NavGraph
import cz.pef.mendelu.examtemplate2024.ui.theme.ExamTemplate2024Theme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalFoundationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExamTemplate2024Theme {
                NavGraph(startDestination = Destination.MainScreen.route)
            }
        }
    }
}
