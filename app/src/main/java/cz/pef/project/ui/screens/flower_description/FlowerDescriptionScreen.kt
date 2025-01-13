package cz.pef.project.ui.screens.flower_description

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.pef.project.communication.Plant
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.elements.FlowerAppBar
import cz.pef.project.ui.elements.FlowerNavigationBar

@Composable
fun FlowerDescriptionScreen(navigation: INavigationRouter, id: Int) {
    val viewModel = hiltViewModel<FlowerDescriptionViewModel>()
    val uiState = viewModel.uiState

    val context = LocalContext.current

    LaunchedEffect(id) {
        viewModel.loadPlantDetails(id)
        viewModel.loadResultsForPlant(id)
    }

    MaterialTheme(
        colorScheme = if (true) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = { FlowerAppBar(title = "Description", navigation = navigation) },
            bottomBar = {
                FlowerNavigationBar(
                    navigation = navigation,
                    selectedItem = "Description",
                    id = id
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    // Jméno kytky s edit tlačítkem
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = uiState.name ?: "",
                            style = MaterialTheme.typography.titleLarge
                        )
                        IconButton(onClick = { viewModel.showEditNameDialog() }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Name")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tabulka s daty zasazení a úmrtí
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Plant Date: ${uiState.plantDate ?: "N/A"}")
                            Text("Death Date: ${uiState.deathDate ?: "N/A"}")
                        }
                        IconButton(onClick = { viewModel.showEditDatesDialog() }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Edit Dates")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (uiState.isEditingDescription) {
                                OutlinedTextField(
                                    value = uiState.description ?: "",
                                    onValueChange = { viewModel.updateDescription(it) },
                                    label = { Text("Description") },
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f) // Limit width so the icon button can still fit
                                        .height(200.dp) // Height for the text input
                                        .verticalScroll(rememberScrollState()) // Scrollable text field
                                )
                            } else {
                                Text(
                                    text = uiState.description ?: "No description available",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        // Save/Edit button always visible, ensuring it stays aligned at the end
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp) // Add some space above the button
                                .wrapContentHeight(), // This ensures the row doesn't expand unnecessarily
                            horizontalArrangement = Arrangement.End // Keep the button aligned to the end
                        ) {
                            IconButton(
                                onClick = {
                                    if (uiState.isEditingDescription) {
                                        viewModel.saveDescriptionToDatabase() // Save description
                                    }
                                    viewModel.toggleDescriptionEdit() // Toggle edit mode
                                }
                            ) {
                                Icon(
                                    imageVector = if (uiState.isEditingDescription) Icons.Default.CheckCircle else Icons.Default.Edit,
                                    contentDescription = if (uiState.isEditingDescription) "Save Description" else "Edit Description"
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

// LazyColumn pro zobrazení výsledků
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.results) { result ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    // Zobrazení čísla výsledku a podmínky na samostatném řádku
                                    Text("Result #${result.number}: ${result.condition}")

                                    val annotatedDescription = buildAnnotatedString {
                                        val urlStart = result.description.indexOf("http")
                                        if (urlStart != -1) {
                                            val url = result.description.substring(urlStart)
                                            val textBeforeUrl = result.description.substring(0, urlStart)

                                            append(textBeforeUrl)

                                            pushStringAnnotation(tag = "URL", annotation = url)
                                            withStyle(
                                                style = SpanStyle(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    textDecoration = TextDecoration.Underline
                                                )
                                            ) {
                                                append(url)
                                            }
                                            pop()
                                        } else {
                                            append(result.description)
                                        }
                                    }

                                    // Popis s odkazy jako ClickableText
                                    ClickableText(
                                        text = annotatedDescription,
                                        style = MaterialTheme.typography.bodyMedium,
                                        onClick = { offset ->
                                            annotatedDescription.getStringAnnotations("URL", start = offset, end = offset)
                                                .firstOrNull()?.let { annotation ->
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                                    context.startActivity(intent)
                                                }
                                        }
                                    )
                                }
                            }
                        }
                    }



                }

                if (uiState.isEditNameDialogVisible) {
                    EditNameDialog(
                        currentName = uiState.name ?: "",
                        onSave = { newName ->
                            viewModel.updateName(newName)
                            viewModel.saveNameToDatabase(newName) // Uloží do databáze
                            viewModel.dismissDialogs()
                        },
                        onCancel = { viewModel.dismissDialogs() }
                    )
                }

                if (uiState.isEditDatesDialogVisible) {
                    EditDatesDialog(
                        currentPlantDate = uiState.plantDate,
                        currentDeathDate = uiState.deathDate,
                        onSave = { plantDate, deathDate ->
                            viewModel.updateDates(plantDate, deathDate)
                            viewModel.saveDatesToDatabase(plantDate, deathDate) // Uloží do databáze
                            viewModel.dismissDialogs()
                        },
                        onCancel = { viewModel.dismissDialogs() }
                    )
                }

            }
        )
    }
}


@Composable
fun EditDatesDialog(
    currentPlantDate: String?,
    currentDeathDate: String?,
    onSave: (String?, String?) -> Unit,
    onCancel: () -> Unit
) {
    var plantDate by remember { mutableStateOf(currentPlantDate) }
    var deathDate by remember { mutableStateOf(currentDeathDate) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Edit Dates") },
        text = {
            Column {
                DatePickerDialog(
                    label = "Plant Date",
                    initialDate = plantDate,
                    onDateSelected = {
                        plantDate = it
                        if (deathDate != null && plantDate != null && plantDate!! > deathDate!!) {
                            errorMessage = "Plant date cannot be after death date."
                        } else {
                            errorMessage = null
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                DatePickerDialog(
                    label = "Death Date",
                    initialDate = deathDate,
                    onDateSelected = {
                        deathDate = it
                        if (plantDate != null && deathDate != null && plantDate!! > deathDate!!) {
                            errorMessage = "Death date cannot be before plant date."
                        } else {
                            errorMessage = null
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                errorMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (errorMessage == null) {
                        onSave(plantDate, deathDate)
                    }
                },
                enabled = errorMessage == null
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DatePickerDialog(label: String, initialDate: String?, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val date = Calendar.getInstance()
            date.set(year, month, dayOfMonth)
            onDateSelected(dateFormatter.format(date.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        TextButton(onClick = { datePickerDialog.show() }) {
            Text(initialDate ?: "Select Date")
        }
    }
}

@Composable
fun EditNameDialog(currentName: String, onSave: (String) -> Unit, onCancel: () -> Unit) {
    var newName by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Edit Name") },
        text = {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(newName)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}
