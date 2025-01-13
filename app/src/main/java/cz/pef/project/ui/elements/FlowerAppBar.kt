package cz.pef.project.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cz.pef.project.R
import cz.pef.project.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerAppBar(
    title: String, navigation: INavigationRouter
) {
    TopAppBar(title = { Text(text = title) }, navigationIcon = {
        IconButton(onClick = { navigation.navigateToGardenOverviewScreen() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }
    }, actions = {
        IconButton(onClick = { navigation.navigateToUserSettingsScreen() }) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = stringResource(R.string.user_settings)
            )
        }
    })
}
