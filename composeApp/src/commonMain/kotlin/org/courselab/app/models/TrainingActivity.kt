package org.courselab.app.models

import kotlinx.serialization.Serializable

@Serializable
data class TrainingActivity(
    val activityID: Long,
    val user: User, // Assuming User is another data class or handled appropriately
    val date: String, // Using String for LocalDateTime, consider Kotlinx datetime for better handling
    val distanciaMetros: Float,
    val tiempoSegundos: Float,
    val ritmoMinKm: Float,
    val activityNotes: String,
    val laps: List<Lap>
)

