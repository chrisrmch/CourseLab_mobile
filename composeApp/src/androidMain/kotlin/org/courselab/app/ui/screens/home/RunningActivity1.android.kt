package org.courselab.app.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

@Composable
actual fun TargetMapboxMap(modifier: Modifier) {
    MapboxMap(
        modifier,
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(4.0)
                center(Point.fromLngLat(-98.0, 39.5))
                pitch(2.0)
                bearing(0.0)
            }
        },
    )
}