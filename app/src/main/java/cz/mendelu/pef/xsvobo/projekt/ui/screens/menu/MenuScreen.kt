package cz.mendelu.pef.xsvobo.projekt.ui.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.xsvobo.projekt.R
import cz.mendelu.pef.xsvobo.projekt.model.Set
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navigationRouter: INavigationRouter) {
    val sets: MutableList<Set> = mutableListOf()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Menu",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {
        MenuScreenContent(
            paddingValues = it,
            sets = sets,
            navigationRouter = navigationRouter
        )
    }
}

@Composable
fun MenuScreenContent(
    paddingValues: PaddingValues,
    sets: List<Set>,
    navigationRouter: INavigationRouter
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Set the background color to gray
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_emoji_emotions_24),
                    contentDescription = "",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "FlashCards", color = Color.White)
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                navigationRouter.navigateToSetListScreen(null)
            }
        ) {
            Text(text = "Show Sets")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                navigationRouter.navigateToCodeSetScreen(null)
            }
        ) {
            Text(text = "Add set from code")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(text = "Last Played Set")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            sets.forEach {
                item {
                    LastSetRow(
                        set = it,
                        onClick = {
                            navigationRouter.navigateToPlaySetScreen(it.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LastSetRow(
    set: Set,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = set.name)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "")
        }
    }
}
