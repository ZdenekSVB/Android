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
import cz.pef.project.communication.Properties

@Composable
fun GardenCenterBottomSheet(properties: Properties, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Selected Garden Center:\nName: ${properties.name} \nDescription: ${properties.description} \nAddress:${properties.address}",
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
