package cz.pef.project.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerTopAppBar(title: String) {
    TopAppBar(
    title = { Text(title) },
    navigationIcon = {
        IconButton(onClick = { /* Handle navigation icon click */ }) {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
        }
    },
    actions = {
        IconButton(onClick = { /* Handle user settings */ }) {
            Icon(Icons.Default.AccountCircle, contentDescription = "User Settings")
        }
    }
    )
}