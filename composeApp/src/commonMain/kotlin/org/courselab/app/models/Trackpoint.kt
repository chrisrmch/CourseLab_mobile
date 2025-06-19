package org.courselab.app.models

import kotlinx.serialization.Serializable

@Serializable
data class TrackpointDTO(
    val idTrackpoint: Long? = null,
    val time: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val altitudeMeters: Float? = null,
    val distanceMeters: Float? = null,
    val extension: TrackpointExtensionDTO? = null
)

@Serializable
data class TrackpointExtensionDTO(
    val idTrackpointExtension: Long? = null,
    val speed: Float? = null,
    val runCadence: Int? = null
)