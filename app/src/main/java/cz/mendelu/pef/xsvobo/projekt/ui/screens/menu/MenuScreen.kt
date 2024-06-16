package cz.mendelu.pef.xsvobo.projekt.ui.screens.menu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import cz.mendelu.pef.xsvobo.projekt.R
import cz.mendelu.pef.xsvobo.projekt.model.Set
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navigationRouter: INavigationRouter) {
    val viewModel = hiltViewModel<MenuScreenViewModel>()
    val state = viewModel.menuScreenUIState.collectAsState()

    // Ensure latest sets are loaded when MenuScreen is recomposed
    LaunchedEffect(key1 = Unit) {
        viewModel.loadLatestSet()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.menu_title),
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
            sets = (state.value as? MenuScreenUIState.Success)?.sets ?: emptyList(),
            navigationRouter = navigationRouter,
            viewModel = viewModel
        )
    }
}

@Composable
fun MenuScreenContent(
    viewModel: MenuScreenViewModel,
    paddingValues: PaddingValues,
    sets: List<Set>,
    navigationRouter: INavigationRouter
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Box with emoji and text
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
                Text(text = stringResource(id = R.string.flashcards_box), color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to show sets
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
            Text(text = stringResource(id = R.string.show_sets))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button for app info
        Button(
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                navigationRouter.navigateToAppInfoScreen()
            }
        ) {
            Text(text = stringResource(id = R.string.app_info_button))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Last played set section title
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.last_played_set))
        }

        Spacer(modifier = Modifier.height(16.dp))

        val setIconUrls by viewModel.setIconUrls.collectAsState()

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(sets) { set ->
                val iconUrl = setIconUrls[set.id]
                LastSetRow(
                    set = set,
                    setIconUrl = iconUrl,
                    onClick = {
                        navigationRouter.navigateToPlaySetScreen(set.id)
                    }
                )
            }
        }
    }
}

@Composable
fun LastSetRow(
    setIconUrl: String?,
    set: Set,
    onClick: () -> Unit

) {
    val coroutineScope = rememberCoroutineScope()
    val iconSize = 48.dp

    val context = LocalContext.current
    val imageFile = setIconUrl?.let { File(context.filesDir, it) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Log.d("SetIcon", "" + set.icon)
            if (set.icon == null || set.icon!!.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(iconSize)
                        .drawBehind {
                            drawCircle(
                                color = Color.hsl(230F, 0.89F, 0.64F),
                                radius = this.size.minDimension / 2
                            )
                        }
                ) {
                    Text(
                        text = set.name.substring(0, 1),
                        color = Color.White,
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            } else {
                AsyncImage(
                    model = imageFile?.toUri(),
                    contentDescription = "Profile Image",
                    placeholder = painterResource(R.drawable.placeholder),
                    modifier = Modifier
                        .size(iconSize)
                        .clip(CircleShape)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column {
            Text(
                text = if (set.name != "Set") "${set.name}" else stringResource(id = R.string.set_name)
            )
        }
        // Play button
        Column {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "")
        }
    }
}
