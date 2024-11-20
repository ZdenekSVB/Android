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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerNavBar(onNavigate: (String) -> Unit, selectedItem: String) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "Description") },
            label = { Text("Description") },
            selected = true,
            onClick = { /* Navigate to Description */ }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_image_24), contentDescription = "AI") },
            label = { Text("Pictures") },
            selected = false,
            onClick = { /* Navigate to Pictures */ }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_emoji_emotions_24), contentDescription = "AI") },
            label = { Text("AI") },
            selected = false,
            onClick = { /* Navigate to AI */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.LocationOn, contentDescription = "Map") },
            label = { Text("Map") },
            selected = false,
            onClick = { /* Navigate to Map */ }
        )
    }
}