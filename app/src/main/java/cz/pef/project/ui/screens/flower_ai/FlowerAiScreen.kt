package cz.pef.project.ui.screens.flower_ai

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.withStyle

@Composable
fun FlowerAiScreen(navigation: INavigationRouter, id: Int) {
    val viewModel = hiltViewModel<FlowerAiViewModel>()
    val uiState = viewModel.uiState.value
    val isDarkTheme by viewModel.isDarkTheme.collectAsState() // Sledujeme nastavení tmavého režimu


    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { viewModel.selectImage(it) }
        }

    val context = LocalContext.current

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = { FlowerAppBar(title = "AI", navigation = navigation) },
            bottomBar = {
                FlowerNavigationBar(
                    navigation = navigation,
                    selectedItem = "AI",
                    id = id
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { launcher.launch("image/*") }, // Kliknutím spustí výběr obrázku
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.selectedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(uiState.selectedImageUri),
                                contentDescription = "Selected Flower",
                                modifier = Modifier.matchParentSize()
                            )
                        } else {
                            Text(
                                text = "Select Image",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.analyzeImage(id) },
                        enabled = uiState.selectedImageUri != null
                    ) {
                        Text("Analyze Image")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!uiState.analysisResult.isNullOrEmpty()) {
                        Text(
                            text = "Results:",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val annotatedText = buildAnnotatedString {
                            uiState.analysisResult.split("\n").forEach { line ->
                                val parts = line.split(" (")
                                if (parts.size == 2) {
                                    val title = parts[0]
                                    val urlStart = title.indexOf("https://")
                                    val url = if (urlStart >= 0) title.substring(urlStart) else null

                                    if (url != null) {
                                        append("Detected: ")
                                        pushStringAnnotation(tag = "URL", annotation = url)
                                        withStyle(
                                            style = SpanStyle(
                                                color = MaterialTheme.colorScheme.primary,
                                                textDecoration = TextDecoration.Underline
                                            )
                                        ) {
                                            append(title.substringBefore("https://"))
                                        }
                                        pop()
                                        append(" (")
                                        append(parts[1])
                                    } else {
                                        withStyle(
                                            style = SpanStyle(
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        ) {
                                            append(line)
                                        }
                                    }
                                }
                                append("\n")
                            }
                        }

                        ClickableText(
                            text = annotatedText,
                            style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                            onClick = { offset ->
                                annotatedText.getStringAnnotations(
                                    tag = "URL",
                                    start = offset,
                                    end = offset
                                )
                                    .firstOrNull()?.let { annotation ->
                                        val intent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                        context.startActivity(intent)
                                    }
                            }
                        )
                    }
                }
            }
        )
    }
}
