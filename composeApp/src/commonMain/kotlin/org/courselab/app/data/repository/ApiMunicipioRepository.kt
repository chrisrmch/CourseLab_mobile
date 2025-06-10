package org.courselab.app.org.courselab.app.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.courselab.app.org.courselab.app.models.MunicipioSearchResult

interface MunicipioRepository {
    suspend fun searchMunicipios(query: String): List<MunicipioSearchResult>
}

@Serializable
private data class GeoApiResponse(
    val data: List<MunicipioDto>
)

@Serializable
private data class MunicipioDto(
    @SerialName("CMUN") val codigo: String,
    @SerialName("DMUN50") val nombre: String
)


class ApiMunicipioRepository(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val apiKey: String
) : MunicipioRepository {

    private var json: Json = Json { ignoreUnknownKeys = true }

    override suspend fun searchMunicipios(query: String): List<MunicipioSearchResult> {
        println("DEBUGGG Iniciando petición para query='$query'")

        // 1) Hacemos la petición y capturamos todo el HttpResponse
        val httpResponse: HttpResponse = httpClient.get(baseUrl) {
            parameter("KEY", apiKey)
            parameter("QUERY", query)
            parameter("FORMAT", "json")
            parameter("PAGE_SIZE", 1000)
        }

        // 2) Logueamos el código de estado
        println("DEBUGGG Status = ${httpResponse.status.value}")

        // 3) Leemos el cuerpo como texto (raw JSON)
        val rawJson = httpResponse.bodyAsText()
        println("DEBUGGG Raw JSON = $rawJson")

        // 4) Intentamos parsearlo manualmente, para que veas si falla
        val wrapper: GeoApiResponse = try {
            json
                .decodeFromString(GeoApiResponse.serializer(), rawJson)
        } catch (e: Exception) {
            println("DEBUGGG Error parseando JSON: ${e.message}")
            // si hay error de parseo, devolvemos lista vacía o lo que estimes conveniente
            return emptyList()
        }

        println("DEBUGGG Lista de DTOs = ${wrapper.data.size} elementos")

        // 5) Mapeamos sólo el nombre
        return wrapper.data.map { dto ->
            MunicipioSearchResult(
                comunidad_autonoma_ID = null,
                provincia_ID = null,
                municipio_ID = dto.codigo.toIntOrNull() ?: 0,
                municipio = dto.nombre
            )
        }
    }
}
class FakeMunicipioRepository : MunicipioRepository {
    private val sample = listOf(
        MunicipioSearchResult(comunidad_autonoma_ID = null, provincia_ID = null, municipio_ID = 1, municipio = "Madrid"),
        MunicipioSearchResult(comunidad_autonoma_ID = null, provincia_ID = null, municipio_ID = 2, municipio = "Barcelona"),
        MunicipioSearchResult(comunidad_autonoma_ID = null, provincia_ID = null, municipio_ID = 3, municipio = "Valencia"),
        MunicipioSearchResult(comunidad_autonoma_ID = null, provincia_ID = null, municipio_ID = 4, municipio = "Sevilla")
    )

    override suspend fun searchMunicipios(query: String): List<MunicipioSearchResult> {
        return sample.filter { it.municipio.contains(query, ignoreCase = true) }
    }
}
