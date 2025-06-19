package org.courselab.app.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ActividadDTO(
    @SerialName("id_actividad") val idActividad: Long? = null,
    @SerialName("us") val idUsuario: Long? = null,
    val fecha: String,
    val laps: List<LapDTO>
)

@Serializable
data class ActividadDTORequest(
    val idUsuario: Long? = null,
    val fecha: String,
    val laps: List<LapDTO>
)




