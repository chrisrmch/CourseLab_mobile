package org.courselab.app.ui.screens.homeNavigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.courselab.app.LocalLocationPermissionGranted
import org.courselab.app.R
import org.courselab.app.data.repository.ActivityRepository
import org.courselab.app.ui.screens.activity.ActivityViewModel
import org.courselab.app.viewmodel.getViewModelScope
import org.koin.compose.koinInject


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("MissingPermission")
@Composable
actual fun TargetMapboxMap(
    modifier: Modifier,
    navController: NavHostController,
) {
    val activityViewModel = koinInject<ActivityViewModel>()
    val uiState by activityViewModel.state.collectAsState()
    val locationGranted = LocalLocationPermissionGranted.current
    var mapViewRef by remember { mutableStateOf<MapView?>(null) }
    val activityRepository = koinInject<ActivityRepository>()

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(-98.0, 39.5))
            zoom(4.0)
        }
    }

    val paceDisplay = rememberLastPaceText(uiState.smoothedPaceSec)

    LaunchedEffect(locationGranted) {
        if (locationGranted) {
            mapViewportState.transitionToFollowPuckState()
        }
    }

    LaunchedEffect(uiState.isRunning) {
        if (uiState.isRunning) {
            while (true) {
                delay(1_000)
                activityViewModel.incrementTime()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            scaleBar = {
                ScaleBar(
                    Modifier
                        .statusBarsPadding()
                        .padding(start = 4.dp)
                )
            },
            compass = {
                Compass(
                    Modifier
                        .statusBarsPadding()
                        .padding(start = 4.dp)
                )
            },
            logo = {
                Logo(
                    Modifier
                        .statusBarsPadding()
                        .padding(start = 4.dp)
                )
            },
        ) {
            MapEffect(Unit) { mapView ->
                mapView.mapboxMap.setBounds(
                    CameraBoundsOptions.Builder()
                        .minZoom(4.0)
                        .maxZoom(18.0)
                        .build()
                )

                if (locationGranted) {
                    mapView.location.updateSettings {
                        enabled = true
                        pulsingEnabled = true
                        locationPuck = createDefault2DPuck(withBearing = true)
                        puckBearing = PuckBearing.COURSE
                    }
                }
                mapViewRef = mapView
            }

            DisposableEffect(mapViewRef, locationGranted, uiState.isRunning) {
                if (mapViewRef != null && locationGranted && uiState.isRunning) {
                    val listener = OnIndicatorPositionChangedListener { point ->
                        activityViewModel.appendLocation(point)
                    }
                    mapViewRef!!.location.addOnIndicatorPositionChangedListener(listener)
                    onDispose {
                        mapViewRef!!.location.removeOnIndicatorPositionChangedListener(listener)
                    }
                } else {
                    onDispose { }
                }
            }


            val geoPoints = remember(uiState.trackPoints) {
                uiState.trackPoints.map { it.point }
            }

            if (geoPoints.size >= 2) {
                PolylineAnnotation(
                    points = geoPoints
                ) {
                    lineColor = Color(10, 96, 209)
                    lineWidth = 10.0
                    lineBlur = 0.2
                }
            }
        }

        IconButton(
            onClick = {
                getViewModelScope().launch {
                    activityRepository.getAllUserActivities()
                    navController.popBackStack()
                }
                      },
            modifier = Modifier
                .align(Alignment.TopStart).windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 20.dp, start = 12.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    shape = CircleShape
                )
                .size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "go back",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                .align(Alignment.BottomCenter)
                .padding(vertical = 24.dp, horizontal = 8.dp)
        ) {
            FlowRow(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoBlock(label = "Time", value = formatTime(uiState.elapsedTime))
                InfoBlock(label = "Distance", value = "${"%.2f".format(uiState.distance)} km")
                InfoBlock(label = "Pace", value = paceDisplay)
                InfoBlock(
                    label = "Calories",
                    value = "${uiState.trackPoints.sumOf { it.calories.toDouble() }.toInt()} kcal"
                )
            }
            Spacer(Modifier.height(24.dp))
            when {
                !uiState.isRunning && !uiState.isPaused && !uiState.isFinished -> {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CenteredIconButton(
                            icon = Icons.Filled.PlayArrow,
                            tint = Color.White,
                            background = MaterialTheme.colorScheme.primary
                        ) { activityViewModel.startActivity() }
                    }
                }

                uiState.isRunning -> {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CenteredIconButton(
                            icon = Icons.Filled.Close,
                            tint = Color.White,
                            background = MaterialTheme.colorScheme.secondary
                        ) { activityViewModel.pauseActivity() }
                    }
                }

                uiState.isPaused && !uiState.isFinished -> {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButtonRowItem(
                            icon = Icons.Filled.PlayArrow,
                            bgColor = MaterialTheme.colorScheme.primary
                        ) { activityViewModel.resumeActivity() }

                        IconButtonRowItem(
                            icon = Icons.Filled.Done,
                            bgColor = MaterialTheme.colorScheme.error
                        ) { activityViewModel.finishAndUpload {   } }
                    }
                }

                uiState.isFinished -> {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Activity finished!",
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoBlock(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = TextUnit(22f, TextUnitType.Sp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = TextUnit(22f, TextUnitType.Sp)
        )
    }
}


private fun formatTime(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}

@Composable
private fun IconButtonRowItem(
    icon: ImageVector,
    bg: ImageVector? = null,
    bgColor: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(56.dp)
            .background(bgColor, shape = CircleShape),
        colors = IconButtonDefaults.iconButtonColors(containerColor = bgColor)
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
    }
}

@Composable
private fun CenteredIconButton(
    icon: ImageVector,
    tint: Color,
    background: Color,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(72.dp)
            .background(background, shape = CircleShape)
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(36.dp))
    }
}

private const val NO_PACE = 9_999f

@Composable
fun rememberLastPaceText(paceSec: Float?): String {
    val valid = paceSec?.takeIf { it < NO_PACE }
    return if (valid == null) "--:-- /km"
    else "%d:%02d /km".format(valid.toInt() / 60, valid.toInt() % 60)
}
