package cz.mendelu.pef.xsvobo.projekt.ui.screens.setList


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import cz.mendelu.pef.xsvobo.projekt.R
import cz.mendelu.pef.xsvobo.projekt.model.Set
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetListScreen(navigationRouter: INavigationRouter) {
    var sets by remember { mutableStateOf<List<Set>>(emptyList()) }
    val viewModel = hiltViewModel<SetListScreenViewModel>()
    var setData by remember { mutableStateOf(SetListScreenData()) }


    viewModel.setListScreenUIState.value.let {
        when (it) {
            is SetListScreenUIState.Loading -> {
                viewModel.loadSets()
            }

            is SetListScreenUIState.Success -> {
                sets = it.sets
            }

            is SetListScreenUIState.SetDeleted -> {
                viewModel.loadSets()
            }

        }
    }

    val snackbarHostState = SnackbarHostState()

    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = {
                navigationRouter.returnBack()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
            }
        }, title = {
            Text(text = stringResource(id = R.string.set_list_title))
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            viewModel.createSet()
        }, content = { Icon(imageVector = Icons.Default.Add, contentDescription = "") })
    }, modifier = Modifier
        .fillMaxSize()
        .padding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(it)
        ) {
            SetListContent(
                sets = sets,
                navigationRouter = navigationRouter,
                viewModel = viewModel,
                snackbarHostState = snackbarHostState
            )
        }
    }

    SnackbarHost(
        hostState = snackbarHostState, modifier = Modifier
    ) {
        Snackbar {
            Text(text = "Cannot play empty set")
        }
    }
}

@Composable
fun SetListContent(
    sets: List<Set>,
    navigationRouter: INavigationRouter,
    viewModel: SetListScreenViewModel,
    snackbarHostState: SnackbarHostState
) {
    val setIconUrls by viewModel.setIconUrls.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(sets) { set ->
            val iconUrl = setIconUrls[set.id]
            SetListRow(
                set = set,
                navigationRouter = navigationRouter,
                viewModel = viewModel,
                snackbarHostState = snackbarHostState,
                setIconUrl = iconUrl,
            )
        }
    }
}


@Composable
fun SetListRow(
    setIconUrl: String?,
    set: Set,
    navigationRouter: INavigationRouter,
    viewModel: SetListScreenViewModel,
    snackbarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()
    val iconSize = 48.dp
    val context = LocalContext.current
    val imageFile = setIconUrl?.let { File(context.filesDir, it) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .background(Color.White)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (set.icon == null || set.icon!!.isEmpty()) {
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .background(Color.hsl(230F, 0.89F, 0.64F))
                    .padding(8.dp)
            ) {
                Text(
                    text = set.name.take(1),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            AsyncImage(
                model = imageFile?.toUri(),
                contentDescription = "Set Icon",
                placeholder = painterResource(R.drawable.placeholder),
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds
            )

        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = set.name.takeUnless { it == "Set" }
                ?: stringResource(id = R.string.set_name),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(onClick = {
            set.id?.let { setId ->
                viewModel.deleteSet(setId)
            }
        }) {
            Icon(
                imageVector = Icons.Default.Delete, contentDescription = "Delete Set"
            )
        }

        IconButton(onClick = {
            set.id?.let { setId ->
                navigationRouter.navigateToCardListScreen(setId)
            }
        }) {
            Icon(
                imageVector = Icons.Default.Edit, contentDescription = "Edit Set"
            )
        }

        IconButton(onClick = {
            if (set.cardsCount <= 0) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Cannot play empty set")
                }
            } else {
                set.id?.let { setId ->
                    navigationRouter.navigateToPlaySetScreen(setId)
                }
            }
        }) {
            Icon(
                imageVector = Icons.Default.PlayArrow, contentDescription = "Play Set"
            )
        }
    }
}