package cz.pef.project.ui.screens.flower_location

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
import cz.pef.project.communication.Feature
import cz.pef.project.ui.elements.FlowerAppBar
import cz.pef.project.ui.elements.FlowerNavigationBar
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.elements.GardenCenterBottomSheet
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import cz.pef.project.map.bitmapDescriptorFromVector
import cz.pef.project.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerLocationScreen(navigation: INavigationRouter, id: Int) {
    val viewModel = hiltViewModel<FlowerLocationViewModel>()
    val uiState = viewModel.uiState.collectAsState()
    var isPlantBottomSheetVisible by remember { mutableStateOf(false) }
    var isGardenBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedGardenCenter by remember { mutableStateOf<Feature?>(null) }

    val isDarkTheme by viewModel.isDarkTheme.collectAsState() // Sledujeme nastavení tmavého režimu

    val context = LocalContext.current

    LaunchedEffect(id) {
        viewModel.loadPlantDetails(id)
    }

    var plantMarkerState = rememberMarkerState(position = LatLng(0.0, 0.0))

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                FlowerAppBar(
                    title = stringResource(id = R.string.location), navigation = navigation
                )
            },
            bottomBar = {
                FlowerNavigationBar(
                    navigation = navigation,
                    selectedItem = stringResource(id = R.string.location),
                    id = id
                )
            },
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (uiState.value.location != null) {
                    plantMarkerState = rememberMarkerState(position = uiState.value.location!!)

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("GoogleMap"),
                        cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(uiState.value.location!!, 10f)
                        }
                    ) {
                        Marker(
                            state = plantMarkerState,
                            title = stringResource(id = R.string.plant_location),
                            draggable = true,
                            onClick = {
                                isPlantBottomSheetVisible = true
                                true
                            }
                        )

                        uiState.value.gardenCenters.forEach { gardenCenter ->
                            Marker(
                                state = rememberMarkerState(
                                    position = LatLng(
                                        gardenCenter.geometry.coordinates[1],
                                        gardenCenter.geometry.coordinates[0]
                                    )
                                ),
                                title = gardenCenter.properties.name,
                                snippet = stringResource(id = R.string.garden_center),
                                icon = bitmapDescriptorFromVector(context, R.drawable.florist),
                                onClick = {
                                    selectedGardenCenter = gardenCenter
                                    isGardenBottomSheetVisible = true
                                    true
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                if (isPlantBottomSheetVisible) {
                    ModalBottomSheet(onDismissRequest = { isPlantBottomSheetVisible = false },
                        content = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                var isBottomSheetVisible = true
                                Text(
                                    text = stringResource(
                                        id = R.string.plant_details,
                                        uiState.value.selectedPlant!!.name,
                                        plantMarkerState.position.latitude,
                                        plantMarkerState.position.longitude,
                                        uiState.value.selectedPlant!!.description
                                            ?: "Description missing"
                                    ),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.align(Alignment.TopStart)
                                )
                                Button(
                                    onClick = {
                                        val plantId = uiState.value.selectedPlant?.id
                                        if (plantId != null) {
                                            viewModel.updatePlantLocation(
                                                plantId = plantId,
                                                newLocation = plantMarkerState.position
                                            )
                                        }
                                        isBottomSheetVisible = false
                                    }, modifier = Modifier.align(Alignment.BottomEnd)
                                ) {
                                    Text(stringResource(id = R.string.save_location))
                                }
                            }
                        })
                }

                if (isGardenBottomSheetVisible) {
                    ModalBottomSheet(onDismissRequest = { isGardenBottomSheetVisible = false },
                        content = {
                            selectedGardenCenter?.let { gardenCenter ->
                                GardenCenterBottomSheet(properties = gardenCenter.properties,
                                    onDismiss = { isGardenBottomSheetVisible = false })
                            }
                        })
                }
            }
        }
    }
}
