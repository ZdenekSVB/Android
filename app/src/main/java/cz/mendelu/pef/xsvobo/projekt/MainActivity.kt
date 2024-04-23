package cz.mendelu.pef.xsvobo.projekt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cz.mendelu.pef.xsvobo.projekt.navigation.Destination
import cz.mendelu.pef.xsvobo.projekt.navigation.NavGraph
import cz.mendelu.pef.xsvobo.projekt.ui.theme.ProjektTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjektTheme {
                NavGraph(startDestination = Destination.MenuScreen.route)
            }
        }
    }
}
