package org.courselab.app.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PerfilUsuarioResponseDTO(
    val idPerfil: Long,
    @SerialName("fotoPerfil")
    val fotoPerfilUrl: String? = null,
    val biografia: String? = null,
    val localizacion: Localizacion? = null,
    val idUsuario: Long? = null,
    val peso: Float? = null,
    val nombre: String? = null,
    val apellidos: String? = null,
    val fechaNacimiento: String? = null,
    val fechaActualizacion: String? = null
)

@Serializable
data class Localizacion(
    val municipio_id: Long,
    val nombre: String,
    val provincia: Povincia
)

@Serializable
data class Povincia(
    val provincia_id: Long,
    val nombre: String,
    val comunidadAutonoma: ComunidadAutonoma
)

@Serializable
data class ComunidadAutonoma(
    val ccaa_id: Long,
    val nombre: String
)
