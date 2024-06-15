package cz.mendelu.pef.xsvobo.projekt.ui.screens.cardList

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.pef.xsvobo.projekt.model.Card
import cz.mendelu.pef.xsvobo.projekt.model.Set
import cz.mendelu.pef.xsvobo.projekt.navigation.INavigationRouter
import cz.mendelu.pef.xsvobo.projekt.ui.screens.setList.SetListScreenData
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import coil.compose.rememberImagePainter
import cz.mendelu.pef.xsvobo.projekt.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    navigationRouter: INavigationRouter,
    id: Long
) {
    var setData by remember { mutableStateOf(SetListScreenData()) }
    var cards by remember { mutableStateOf<List<Card>>(emptyList()) }

    val viewModel = hiltViewModel<CardListScreenViewModel>()
    val state = viewModel.cardListScreenUIState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadSet(id)
    }

    state.value.let {
        when (it) {
            is CardListScreenUIState.Loading -> {
                Log.d("CardListScreen", "Loading")
            }

            is CardListScreenUIState.Success -> {
                cards = it.cards
                Log.d("CardListScreen", "Success with ${cards.size} cards")
            }

            is CardListScreenUIState.SetNameChanged -> {
                setData = it.data
                Log.d("CardListScreen", "SetNameChanged")
            }

            is CardListScreenUIState.CardDeleted -> {
                Log.d("CardListScreen", "CardDeleted")
                viewModel.loadSet(id)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigationRouter.returnBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                },
                title = { Text(text = stringResource(id = R.string.card_list_title)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Log.d("CardListScreen", "FAB clicked")
                setData.set.id?.let { viewModel.addCard(it) }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        },
        modifier = Modifier.fillMaxSize().padding()
    ) {
        CardListScreenContent(
            paddingValues = it,
            cards = cards,
            navigationRouter = navigationRouter,
            setData = setData,
            actions = viewModel
        )
    }
}

@Composable
fun CardListScreenContent(
    paddingValues: PaddingValues,
    cards: List<Card>,
    navigationRouter: INavigationRouter,
    setData: SetListScreenData,
    actions: CardListScreenActions
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.LightGray).padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.background(color = Color.White).padding(16.dp)) {
                Text(text = stringResource(id = R.string.cards) + ": ${cards.size}")
            }

            Box(
                modifier = Modifier.size(120.dp).clip(CircleShape).background(Color.Black).clickable {
                    pickImageLauncher.launch("image/*")
                }
            ) {
                if (selectedImageUri == null) {
                    Text(
                        text = stringResource(id = R.string.select_picture),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    selectedImageUri?.let {
                        Image(
                            painter = rememberImagePainter(it),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize().align(Alignment.Center),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            OutlinedTextField(
                value = setData.set.name,
                onValueChange = {
                    setData.set.name = it
                    actions.setTextChanged(it)
                },
                leadingIcon = {
                    IconButton(onClick = { actions.saveSetName() }) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.background(color = Color.White).padding(16.dp)) {
            Text(text = stringResource(id = R.string.set_generated_code))
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
    card: Card,
    navigationRouter: INavigationRouter,
    actions: CardListScreenActions
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable {}.padding(16.dp).background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(
                    text = card.name.substring(0, 1),
                    modifier = Modifier.padding(16.dp).drawBehind {
                        drawCircle(color = Color.hsl(230F, 0.89F, 0.64F), radius = size.maxDimension)
                    },
                    color = Color.White
                )
            }
            Column {
                Text(text = "${card.name} ${card.id}")
            }
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
