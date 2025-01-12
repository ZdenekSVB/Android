package cz.pef.project.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.pef.project.DB.PlantEntity

@Composable
fun PlantDetailsBottomSheet(plant: PlantEntity, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Plant Details:\nName: ${plant.name} \nLocation: ${plant.latitude}, ${plant.longitude} \nDescription: \n${plant.description} ",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.TopStart)
        )
        Button(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text("Close")
        }
    }
}
