package org.courselab.app.models

import kotlinx.serialization.Serializable

@Serializable
data class Lap(
    val idLap: Long,
    val entrenamiento: TrainingActivity,
    val startTime: String,// TODO(CAMBIAR A LLIBRERIA KOTLINX DATETIME)
    val totalTimeSeconds: Float,
    val distanceMeters: Float,
    val maxSpeed: Float,
    val calories: Int,
    val avgHeartRate: Int,
    val maxHeartRate: Int,
    val intensity: String,
    val triggerMethod: String,
    val extension: LapExtension,
    val trackpoints: List<Trackpoint>
)

@Serializable
data class LapExtension(
    val idLapExtension: Long,
    val lap: Lap,
    val avgSpeed: Float?,
    val avgRunCadence: Int?,
    val maxRunCadence: Int?
)

