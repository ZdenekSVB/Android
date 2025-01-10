package cz.pef.project.ui.screens.flower_ai

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.elements.FlowerAppBar
import cz.pef.project.ui.elements.FlowerNavigationBar

@Composable
fun FlowerAiScreen(navigation: INavigationRouter) {
    val viewModel = hiltViewModel<FlowerAiViewModel>()
    val uiState = viewModel.uiState
    val darkTheme = true // Nastavení dark mode

    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = { FlowerAppBar(title = "Location", navigation = navigation) },
            bottomBar = { FlowerNavigationBar(navigation = navigation, selectedItem = "Map") },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Placeholder obrázku
                    if (uiState.selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(uiState.selectedImageUri),
                            contentDescription = "Selected Flower",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Image Selected",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tlačítko pro výběr obrázku
                    Button(onClick = { viewModel.selectImage() }) {
                        Text("Select Image")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tlačítko pro analýzu obrázku
                    Button(
                        onClick = { viewModel.analyzeImage() },
                        enabled = uiState.selectedImageUri != null
                    ) {
                        Text("Analyze Image")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Výsledky analýzy
                    if (uiState.analysisResult != null) {
                        Text(
                            text = "Results:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.analysisResult,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Odkaz na Wikipedii
                        TextButton(onClick = { viewModel.openWikipedia() }) {
                            Text("Learn more on Wikipedia")
                        }
                    }
                }
            }
        )
    }
}
