package org.courselab.app.data.repository

import org.courselab.app.models.PerfilUsuarioResponseDTO

actual suspend fun uploadUserProfile(
    baseUrl: String,
    token: String,
    perfilUsuarioRequestDTO: PerfilUsuarioRequestDTO,
    fotoBytes: ByteArray?
): PerfilUsuarioResponseDTO {
    TODO("Not yet implemented")
}