package cz.pef.project.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import cz.pef.project.R
import cz.pef.project.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerNavigationBar(
    navigation: INavigationRouter,
    selectedItem: String
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "Description") },
            label = { Text("Description") },
            selected = selectedItem == "Description",
            onClick = { navigation.navigateToFlowerDescriptionScreen() }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_image_24), contentDescription = "Pictures") },
            label = { Text("Pictures") },
            selected = selectedItem == "Pictures",
            onClick = { navigation.navigateToFlowerPicturesScreen() }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_emoji_emotions_24), contentDescription = "AI") },
            label = { Text("AI") },
            selected = selectedItem == "AI",
            onClick = { navigation.navigateToFlowerAiScreen() }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.LocationOn, contentDescription = "Map") },
            label = { Text("Map") },
            selected = selectedItem == "Map",
            onClick = { navigation.navigateToFlowerMapScreen() }
        )
    }
}