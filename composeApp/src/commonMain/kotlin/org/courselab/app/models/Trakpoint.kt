package org.courselab.app.models

import kotlinx.serialization.Serializable

@Serializable
data class Trackpoint(
    val idTrackpoint: Long,
    val lap: Lap,
    val time: String, // TODO(CAMBIAR A LLIBRERIA KOTLINX DATETIME)
    val latitude: Double,
    val longitude: Double,
    val altitudeMeters: Float,
    val distanceMeters: Float,
    val heartRate: Int,
    val extension: TrackpointExtension,
)

@Serializable
data class TrackpointExtension(
    val idTrackpointExtension: Long,
    val trackpoint: Trackpoint,
    val speed: Float?,
    val runCadence: Int?,
)
