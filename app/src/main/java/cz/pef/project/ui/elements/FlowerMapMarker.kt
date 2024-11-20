package cz.pef.project.ui.elements

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun FlowerMapMarker(
    position: LatLng,
    title: String = "Marker",
    snippet: String? = null,
    draggable: Boolean = false,
    onClick: ((Marker) -> Boolean)? = null
) {
    Marker(
        state = MarkerState(position = position),
        title = title,
        snippet = snippet,
        draggable = draggable,
        onClick = onClick ?: { false }
    )
}
