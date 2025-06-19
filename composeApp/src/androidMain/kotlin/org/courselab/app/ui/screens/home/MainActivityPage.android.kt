package org.courselab.app.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import org.courselab.app.R
import org.courselab.app.models.ActividadDTO
import org.courselab.app.screenDetails
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
actual fun ActivityCard(
    modifier: Modifier,
    onClick: (ActividadDTO) -> Unit,
    actividad: ActividadDTO
) {
    val geoPoints = actividad.geoPoints()

    Box(modifier.fillMaxWidth()) {
        Card(
            onClick = { onClick(actividad) },
            shape = RoundedCornerShape(20.dp),
            modifier = modifier.align(Alignment.Center)
                .width(screenDetails().widthDp * 0.9.dp)
                .height(180.dp)
                .shadow(4.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceContainer)
        ) {
            Row(Modifier
                .fillMaxSize()
                .padding(12.dp)) {

                if (geoPoints.size >= 2) {
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        MapboxMap(
                            modifier = Modifier.fillMaxSize(),
                            compass = {},
                            scaleBar = {},
                            logo = {},
                        ) {
                            PolylineAnnotation(points = geoPoints) {
                                lineColor = Color(0xFF0A60D1)
                                lineWidth = 6.0
                            }

                            MapEffect(geoPoints.hashCode()) { mapView ->
                                val mapboxMap = mapView.mapboxMap
                                val padding = EdgeInsets(20.0, 20.0, 20.0, 20.0)
                                val cameraOptions = mapboxMap.cameraForCoordinates(
                                    coordinates = geoPoints,
                                    coordinatesPadding = padding,
                                )
                                mapboxMap.setCamera(cameraOptions)
                            }
                        }
                    }
                }

                Column(
                    Modifier
                        .weight(1.2f)
                        .fillMaxHeight()
                        .padding(start = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        actividad.prettyDate(),
                        style = typography.titleMedium,
                        color = colorScheme.onSurface
                    )
                    Text(
                        "%.2f km".format(actividad.totalDistanceKm()),
                        style = typography.headlineMedium,
                        color = colorScheme.primary
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MiniStat("Duration", formatTime(actividad.totalTimeSec()))
                        MiniStat("Pace", actividad.paceString())
                        MiniStat("Kcal", actividad.totalCalories().toString())
                    }
                }
            }
        }
    }
}


private fun formatTime(totalSec: Long): String =
    "%d:%02d:%02d".format(totalSec / 3600, (totalSec % 3600) / 60, totalSec % 60)

fun ActividadDTO.geoPoints(): List<Point> =
    laps.flatMap { it.trackpoints }
        .filter { it.latitude != null && it.longitude != null }
        .map   { Point.fromLngLat(it.longitude!!, it.latitude!!) }

fun ActividadDTO.totalDistanceKm(): Float =
    laps.sumOf { it.distanceMeters.toDouble() }
        .toFloat() / 1000f

fun ActividadDTO.totalTimeSec(): Long =
    laps.sumOf { it.totalTimeSeconds.toDouble() }
        .roundToInt()
        .toLong()

fun ActividadDTO.totalCalories(): Int =
    laps.sumOf { it.calories }

fun ActividadDTO.paceString(): String {
    val km = totalDistanceKm()
    if (km == 0f) return "--:--"
    val paceSec = (totalTimeSec() / km).roundToInt()
    return "%d:%02d /km".format(paceSec / 60, paceSec % 60)
}

@RequiresApi(Build.VERSION_CODES.O)
fun ActividadDTO.prettyDate(): String = try {
    val outFmt = DateTimeFormatter.ofPattern("d MMM yyyy")
    java.time.LocalDateTime
        .parse(fecha)
        .format(outFmt)
} catch (e: Exception) {
    fecha
}

@Composable
private fun MiniStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = typography.labelSmall)
        Text(value, style = typography.bodyMedium)
    }
}
