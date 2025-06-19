package org.courselab.app.models

import kotlinx.serialization.Serializable

@Serializable
data class
TrainingActivity(
    val activityID: Long,
    val user: User,
    val date: String,
    val distanciaMetros: Float,
    val tiempoSegundos: Float,
    val ritmoMinKm: Float,
    val activityNotes: String,
    val laps: List<LapDTO>
)

