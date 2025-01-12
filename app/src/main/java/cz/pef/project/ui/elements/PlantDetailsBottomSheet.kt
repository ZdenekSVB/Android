package cz.pef.project.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import cz.pef.project.DB.PlantEntity
import cz.pef.project.ui.screens.flower_location.FlowerLocationUiState
import cz.pef.project.ui.screens.flower_location.FlowerLocationViewModel

@Composable
fun PlantDetailsBottomSheet(plant: PlantEntity, uiState: State<FlowerLocationUiState>, viewModel: FlowerLocationViewModel,latLng:LatLng, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var isBottomSheetVisible= true
        Text(
            text = "Plant Details:\nName: ${plant.name} \nLocation: ${plant.latitude}, ${plant.longitude} \nDescription: \n${plant.description} ",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.TopStart)
        )
        Button(
            onClick = {
                val plantId = uiState.value.selectedPlant?.id
                if (plantId != null) {
                    viewModel.updatePlantLocation(
                        plantId = plantId,
                        newLocation = latLng
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
