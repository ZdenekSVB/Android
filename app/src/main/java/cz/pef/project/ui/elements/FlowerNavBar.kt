package cz.pef.project.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cz.pef.project.R
import cz.pef.project.navigation.INavigationRouter

@Composable
fun FlowerNavigationBar(
    navigation: INavigationRouter, selectedItem: String, id: Int
) {
    NavigationBar {
        NavigationBarItem(icon = {
            Icon(
                Icons.Default.Info, contentDescription = stringResource(R.string.description)
            )
        },
            label = { Text(stringResource(R.string.description)) },
            selected = selectedItem == "Description",
            onClick = { navigation.navigateToFlowerDescriptionScreen(id) })
        NavigationBarItem(icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_image_24),
                contentDescription = stringResource(R.string.pictures)
            )
        },
            label = { Text(stringResource(R.string.pictures)) },
            selected = selectedItem == "Pictures",
            onClick = { navigation.navigateToFlowerPicturesScreen(id) })
        NavigationBarItem(icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_emoji_emotions_24),
                contentDescription = stringResource(R.string.ai)
            )
        },
            label = { Text(stringResource(R.string.ai)) },
            selected = selectedItem == "AI",
            onClick = { navigation.navigateToFlowerAiScreen(id) })
        NavigationBarItem(icon = {
            Icon(
                Icons.Default.LocationOn, contentDescription = stringResource(R.string.map)
            )
        },
            label = { Text(stringResource(R.string.map)) },
            selected = selectedItem == "Map",
            onClick = { navigation.navigateToLoadingScreenMap(id) })
    }
}
