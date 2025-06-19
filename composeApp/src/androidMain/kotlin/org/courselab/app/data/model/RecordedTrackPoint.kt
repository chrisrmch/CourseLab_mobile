package org.courselab.app.data.model

import com.mapbox.geojson.Point

data class RecordedTrackPoint(
    val point: Point,
    val timestamp: Long,
    val altitude: Float?,
    val distanceFromLastKm: Float,
    val speedMps: Float,
    val paceSecPerKm: Float,
    val calories: Float,
    val elevationGain: Float,
    val cadence: Int?
)
