package cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import cz.mendelu.pef.xsvobo.projekt.R
import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenData
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    navigationRouter: INavigationRouter, id: Long
) {
    var setData by remember { mutableStateOf(SetListScreenData()) }
    var cards by remember { mutableStateOf<List<Card>>(emptyList()) }

    val viewModel = hiltViewModel<CardListScreenViewModel>()
    val state = viewModel.cardListScreenUIState.collectAsStateWithLifecycle()

    val setIconUrl by viewModel.setIconUrl.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadSet(id)
    }

    state.value.let {
        when (it) {
            is CardListScreenUIState.Loading -> {
            }

            is CardListScreenUIState.Success -> {
                cards = it.cards
            }

            is CardListScreenUIState.SetNameChanged -> {
                setData = it.data
            }

            is CardListScreenUIState.CardDeleted -> {
                viewModel.loadSet(id)
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = { navigationRouter.returnBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ""
                )
            }
        }, title = { Text(text = stringResource(id = R.string.card_list_title)) })
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            setData.set.id?.let { viewModel.addCard(it) }
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    }, modifier = Modifier
        .fillMaxSize()
        .padding()
    ) {
        CardListScreenContent(paddingValues = it,
            cards = cards,
            navigationRouter = navigationRouter,
            setData = setData,
            actions = viewModel,
            setIconUrl = setIconUrl,
            onIconSelected = { uri ->
                viewModel.updateIcon(uri)
            })
    }
}

@Composable
fun CardListScreenContent(
    setIconUrl: String?,
    onIconSelected: (Uri) -> Unit,
    paddingValues: PaddingValues,
    cards: List<Card>,
    navigationRouter: INavigationRouter,
    setData: SetListScreenData,
    actions: CardListScreenActions
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
            selectedImageUri?.let { onIconSelected(it) }
        }
    val context = LocalContext.current
    val imageFile = setIconUrl?.let { File(context.filesDir, it) }
    val iconSize = 68.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color.White)
                    .padding(16.dp)
            ) {
                Text(text = stringResource(id = R.string.cards) + ": ${cards.size}")
            }

            Spacer(modifier = Modifier.width(40.dp))

            if (setData.set.icon == null && selectedImageUri==null) {
                Box(
                    modifier = Modifier
                        .size(iconSize)
                        .clip(CircleShape)
                        .background(Color.hsl(230F, 0.89F, 0.64F))
                        .padding(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.select_picture),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                            .clickable { pickImageLauncher.launch("image/*") }
                        ,
                        fontSize = 15.sp
                    )
                }
            } else {
                AsyncImage(
                    model = imageFile?.toUri(),
                    contentDescription = "Set Icon",
                    placeholder = painterResource(R.drawable.placeholder),
                    modifier = Modifier
                        .size(iconSize)
                        .clip(CircleShape)
                        .clickable { pickImageLauncher.launch("image/*") }
                    ,
                    contentScale = ContentScale.FillBounds
                )

            }

            Spacer(modifier = Modifier.width(5.dp))

            TextField(value = setData.set.name, onValueChange = {
                setData.set.name = it
                actions.setTextChanged(it)
            }, modifier = Modifier
                .weight(1f) // Take remaining space
                .padding(horizontal = 8.dp)
                .background(color = Color.White), // White background
                leadingIcon = {
                    IconButton(onClick = { actions.saveSetName() }) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "")
                    }
                })
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(cards) { card ->
                CardListRow(card = card, navigationRouter = navigationRouter, actions = actions)
            }
        }
    }
}

@Composable
fun CardListRow(
    card: Card, navigationRouter: INavigationRouter, actions: CardListScreenActions
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .background(Color.White)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.hsl(230F, 0.89F, 0.64F))
                    .padding(8.dp)
            ) {
                Text(
                    text = card.name.take(1),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }


        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = if (card.name != "Card") card.name else stringResource(id = R.string.card_name),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        }
        Row {
            IconButton(onClick = { card.id?.let { actions.deleteCard(it) } }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "")
            }
            IconButton(onClick = { navigationRouter.navigateToAddCardScreen(card.id!!) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "")
            }
        }
        }

    }

