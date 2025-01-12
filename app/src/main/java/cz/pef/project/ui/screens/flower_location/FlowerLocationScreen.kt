package cz.pef.project.ui.screens.flower_location

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import cz.pef.project.ui.elements.FlowerAppBar
import cz.pef.project.ui.elements.FlowerNavigationBar
import cz.pef.project.navigation.INavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerLocationScreen(navigation: INavigationRouter, id: Int) {
    val viewModel = hiltViewModel<FlowerLocationViewModel>()
    val uiState = viewModel.uiState.collectAsState()
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        viewModel.loadPlantDetails(id)
    }

    var markerState = rememberMarkerState(position = LatLng(0.00,0.00))



    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold(
            topBar = { FlowerAppBar(title = "Location", navigation = navigation) },
            bottomBar = { FlowerNavigationBar(navigation = navigation, selectedItem = "Location", id = id) },
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (uiState.value.location != null) {
                    markerState=rememberMarkerState(position = uiState.value.location!!)

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(uiState.value.location!!, 10f)
                        }
                    ) {
                        Marker(
                            state = markerState,
                            title = "Selected Location",
                            snippet = "Drag to update location",
                            draggable = true,
                            onClick = {
                                isBottomSheetVisible = true
                                true
                            }
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                if (isBottomSheetVisible) {
                    ModalBottomSheet(
                        onDismissRequest = { isBottomSheetVisible = false },
                        content = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "New Location:\nLat: ${markerState.position.latitude}, " +
                                            "Lng: ${markerState.position.longitude}",
                                    modifier = Modifier.align(Alignment.TopStart)
                                )
                                Button(
                                    onClick = {
                                        val plantId = uiState.value.selectedPlant?.id
                                        if (plantId != null) {
                                            viewModel.updatePlantLocation(
                                                plantId = plantId,
                                                newLocation = markerState.position
                                            )
                                        }
                                        isBottomSheetVisible = false
                                    },
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                ) {
                                    Text("Save Location")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
