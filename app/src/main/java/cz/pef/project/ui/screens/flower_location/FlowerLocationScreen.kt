package cz.pef.project.ui.screens.flower_location

import android.text.Layout
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import cz.pef.project.ui.elements.FlowerAppBar
import cz.pef.project.ui.elements.FlowerNavigationBar
import cz.pef.project.R
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.communication.Properties
import cz.pef.project.map.bitmapDescriptorFromVector
import cz.pef.project.ui.elements.GardenCenterBottomSheet
import cz.pef.project.ui.elements.PlantDetailsBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerLocationScreen(navigation: INavigationRouter, id: Int) {
    val viewModel = hiltViewModel<FlowerLocationViewModel>()
    val uiState = viewModel.uiState.collectAsState()

    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedGardenCenter by remember { mutableStateOf<Properties?>(null) }

    LaunchedEffect(id) {
        viewModel.loadPlantDetails(id) // Načtení detailů rostliny včetně uložené pozice
        Log.d("ABC LOADED FROM DB",uiState.value.location.latitude.toString()+" "+uiState.value.location.longitude.toString())

    }

// MarkerState bude synchronizován s pozicí z databáze
    val markerState = rememberMarkerState(position = uiState.value.location)


// Sledování pozice markeru a její uložení do ViewModelu
    LaunchedEffect(markerState.position) {
        viewModel.updateLocation(markerState.position)
    }

    MaterialTheme(colorScheme = if (true) darkColorScheme() else lightColorScheme()) {
        Scaffold(
            topBar = { FlowerAppBar(title = "Location", navigation = navigation) },
            bottomBar = { FlowerNavigationBar(navigation = navigation, selectedItem = "Location", id = id) },
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(uiState.value.location, 10f)
                    },
                    onMapClick = { latLng ->
                        markerState.position = latLng // Uživatel kliknul na mapu, aktualizuj pozici markeru
                    Log.d("ABC onMapClick",markerState.position.latitude.toString()+" "+markerState.position.longitude.toString())
                    }
                ) {
                    Marker(
                        state = rememberMarkerState(position = uiState.value.location),
                        title = "Selected Location",
                        snippet = "Drag or click to move",
                        draggable = true, // Povolení přetahování markeru
                        onClick = {
                            selectedGardenCenter = null
                            isBottomSheetVisible = true
                            true
                        }
                    )
                    Log.d("ABC Marker State",uiState.value.location.latitude.toString()+" "+uiState.value.location.longitude.toString())

                    // Zahradnická centra
                    val context = LocalContext.current
                    val gardenCenters = uiState.value.gardenCenters

                    gardenCenters.forEach { gardenCenter ->
                        val latLng = LatLng(
                            gardenCenter.geometry.coordinates[1],
                            gardenCenter.geometry.coordinates[0]
                        )

                        Marker(
                            state = rememberMarkerState(position = latLng),
                            title = gardenCenter.properties.name,
                            snippet = gardenCenter.properties.description,
                            icon = bitmapDescriptorFromVector(context, R.drawable.florist),
                            onClick = {
                                selectedGardenCenter = gardenCenter.properties
                                isBottomSheetVisible = true
                                true
                            }
                        )
                    }
                }

                uiState.value.error?.let { error ->
                    Text(
                        text = "Error: ${error.message}",
                        modifier = Modifier.align(Alignment.BottomCenter),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (isBottomSheetVisible) {
                ModalBottomSheet(
                    onDismissRequest = { isBottomSheetVisible = false },
                    content = {
                        when {
                            selectedGardenCenter != null -> {
                                GardenCenterBottomSheet(
                                    properties = selectedGardenCenter!!,
                                    onDismiss = { isBottomSheetVisible = false }
                                )
                            }
                            else -> {

                                Log.d("ABC Save Location",uiState.value.location.latitude.toString()+" "+uiState.value.location.longitude.toString())

                                Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                    Text(
                                        text = "Current Location: \nLat: ${uiState.value.location.latitude}, " +
                                                "Lng: ${uiState.value.location.longitude}",
                                        modifier = Modifier.align(Alignment.TopStart)
                                    )
                                    Button(
                                        onClick = {
                                            viewModel.updateLocation(markerState.position) // Aktualizace pozice ve ViewModelu
                                            viewModel.saveLocation(id) // Uložení nové pozice do databáze
                                            isBottomSheetVisible = false
                                        },
                                        modifier = Modifier.align(Alignment.BottomEnd)
                                    ) {
                                        Text("Save Location")
                                    }

                                }
                            }
                        }
                    }
                )
            }

        }
    }
}
