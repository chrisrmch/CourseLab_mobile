package org.courselab.app.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.models.PerfilUsuarioResponseDTO
import org.courselab.app.ui.screens.onboarding.viewModel.Sex
import org.courselab.app.ui.screens.profile.UsuarioResponseDTO


class UserRepository(
    private val client: HttpClient,
    private val serverURL: String,
    private val userPrefs: UserPreferencesDataStore
) {
    suspend fun getUserID(): Int = userPrefs.userId.first()
        ?: throw IllegalStateException("No hay userId en UserPreferencesDataStore")

    suspend fun getCurrentUser(): UsuarioResponseDTO? {
        val idUsuario = userPrefs.userId.first() ?: return null
        val token     = userPrefs.sessionToken.first() ?: return null

        return client.get("$serverURL/perfilUsuario/$idUsuario") {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun subirPerfil(
        nombre: String,
        fechaNacimiento: LocalDate?,
        peso: Float,
        fotoBytes: ByteArray?,
        municipioId: Long?,
        provinciaId: Long?,
        ccaaId: Long?,
        sex: Sex?,
        objetvo: Int
    ): PerfilUsuarioResponseDTO {
        val idUsuario = userPrefs.userId.first()
        val token = userPrefs.sessionToken.first()

        println("MUNICIPIOID: $municipioId")
        println("provinciaID: $provinciaId")

        return uploadUserProfile(
            baseUrl = serverURL,
            token = token!!,
            perfilUsuarioRequestDTO = PerfilUsuarioRequestDTO(
                idUsuario = idUsuario!!.toLong(),
                nombre = nombre,
                municipioId = municipioId ?: -1,
                provinciaId = provinciaId ?: -1,
                genero = sex,
                peso = peso,
                objetivo = objetvo,
                fechaNacimiento = fechaNacimiento
            ),
            fotoBytes = fotoBytes
        )
    }
}


expect suspend fun uploadUserProfile(
    baseUrl: String,
    token: String,
    perfilUsuarioRequestDTO: PerfilUsuarioRequestDTO,
    fotoBytes: ByteArray? = null,
): PerfilUsuarioResponseDTO


@Serializable
data class PerfilUsuarioRequestDTO(
    val idUsuario: Long,
    val nombre: String,
    val municipioId: Long,
    val provinciaId: Long,
    val genero: Sex?,
    val peso: Float,
    val objetivo: Int,
    val fechaNacimiento: LocalDate?
)