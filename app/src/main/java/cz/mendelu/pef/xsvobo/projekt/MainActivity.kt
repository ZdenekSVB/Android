package cz.mendelu.pef.xsvobo.projekt

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.preferencesDataStore
import cz.mendelu.pef.xsvobo.projekt.navigation.Destination
import cz.mendelu.pef.xsvobo.projekt.navigation.NavGraph
import cz.mendelu.pef.xsvobo.projekt.ui.theme.ProjektTheme
import dagger.hilt.android.AndroidEntryPoint



val Context.dataStore by preferencesDataStore(name = "my_data_store")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            ProjektTheme {
                NavGraph(startDestination = Destination.MenuScreen.route)
            }
        }
    }
}