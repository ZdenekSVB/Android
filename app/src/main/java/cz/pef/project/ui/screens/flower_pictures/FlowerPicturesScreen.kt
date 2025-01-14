package cz.pef.project.ui.screens.flower_pictures

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import cz.pef.project.communication.Picture
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.elements.FlowerAppBar
import cz.pef.project.ui.elements.FlowerNavigationBar
import cz.pef.project.R


@Composable
fun FlowerPicturesScreen(navigation: INavigationRouter, id: Int) {
    val viewModel = hiltViewModel<FlowerPicturesViewModel>()
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    val isDarkTheme by viewModel.isDarkTheme.collectAsState() // Sledujeme nastavení tmavého režimu

    val imageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.addPictureToPlant(pictureUri = uri.toString(),
                    plantId = id,
                    onError = { errorMessage ->
                        // Show error message to user
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    })
            }
        }

    LaunchedEffect(Unit) {
        viewModel.loadPicturesFromDatabase(id)
    }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(topBar = {
            FlowerAppBar(
                title = stringResource(id = R.string.pictures), navigation = navigation
            )
        }, bottomBar = {
            FlowerNavigationBar(
                navigation = navigation,
                selectedItem = stringResource(id = R.string.pictures),
                id = id
            )
        }, floatingActionButton = {
            FloatingActionButton(onClick = { imageLauncher.launch("image/*") }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_picture)
                )
            }
        }) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (uiState.pictures.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.no_pictures_available),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.pictures.size) { index ->
                            val picture = uiState.pictures[index]
                            PictureItem(picture = picture, onNameChange = { newName ->
                                viewModel.updatePictureName(picture.url, newName)
                            }, onDelete = {
                                viewModel.deletePicture(picture.url)
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PictureItem(
    picture: Picture, onNameChange: (String) -> Unit, onDelete: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(picture.name) }
    var showImageDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(8.dp)
    ) {
        // Image
        AsyncImage(
            model = Uri.parse(picture.url),
            contentDescription = stringResource(id = R.string.description),
            placeholder = painterResource(R.drawable.baseline_image_24),
            error = painterResource(R.drawable.baseline_image_24),
            modifier = Modifier
                .fillMaxSize()
                .clickable { showImageDialog = true }, // Open dialog on click
            contentScale = ContentScale.Crop
        )

        // Name change and edit state
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(8.dp)
        ) {
            if (isEditing) {
                BasicTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = stringResource(id = R.string.save),
                    style = TextStyle(color = Color.Green, fontSize = 12.sp),
                    modifier = Modifier
                        .clickable {
                            onNameChange(editedName)
                            isEditing = false
                        }
                        .padding(top = 8.dp)
                        .align(Alignment.BottomEnd))
            } else {
                Text(
                    text = picture.name,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isEditing = true }
                        .padding(4.dp),
                    maxLines = 1
                )
            }
        }

        // Delete icon
        IconButton(
            onClick = onDelete, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete),
                tint = Color.White
            )
        }

        // Image dialog for enlarged view
        if (showImageDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showImageDialog = false },
                text = {
                    Image(
                        painter = rememberAsyncImagePainter(picture.url),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                },
                confirmButton = {
                    Text(
                        text = stringResource(id = R.string.close),
                        modifier = Modifier
                            .clickable { showImageDialog = false }
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    }
}
