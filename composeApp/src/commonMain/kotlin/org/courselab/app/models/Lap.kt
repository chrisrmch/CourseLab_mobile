package org.courselab.app.models

import kotlinx.serialization.Serializable

@Serializable
data class LapDTO(
    val idLap: Long? = null,
    val startTime: String,
    val totalTimeSeconds: Float,
    val distanceMeters: Float,
    val maxSpeed: Float,
    val calories: Int,
    val extension: LapExtensionDTO? = null,
    val trackpoints: List<TrackpointDTO> = emptyList()
)

@Serializable
data class LapExtensionDTO(
    val idLapExtension: Long? = null,
    val avgSpeed: Float? = null,
    val avgRunCadence: Int? = null,
    val maxRunCadence: Int? = null
)