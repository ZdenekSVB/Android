package cz.pef.project.ui.screens.flower_location

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import cz.pef.project.navigation.INavigationRouter
import cz.pef.project.ui.elements.FlowerNavBar
import cz.pef.project.ui.elements.FlowerTopAppBar
import cz.pef.project.ui.elements.FlowerMapMarker

@Composable
fun FlowerLocationScreen(navigation: INavigationRouter) {
    val viewModel = hiltViewModel<FlowerLocationViewModel>()
    val uiState = viewModel.uiState.collectAsState()

    // Nastavení výchozí polohy kamery
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uiState.value.location, 10f)
    }
    Scaffold(
        topBar = { FlowerTopAppBar(title = "Location") },
        bottomBar = {
            FlowerNavBar(
                onNavigate = { /* Navigate to other screens */ },
                selectedItem = "Location"
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    viewModel.updateLocation(latLng)
                }
            ) {
                Marker(
                    state = MarkerState(position = uiState.value.location),
                    title = "Selected Location",
                    snippet = "Drag or click to move",
                    draggable = true,
                    onClick = {
                        // Reakce na kliknutí
                        true
                    }
                )
            }

            uiState.value.error?.let { error ->
                Text(
                    text = "Error: ${error.message}",
                    modifier = Modifier.align(Alignment.BottomCenter),
                    color = Color.Red
                )
            }
        }
    }
}
