package org.courselab.app.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.courselab.app.models.MunicipioSearchResult

private fun String.toSentenceCase(): String {
    if (isBlank()) return this
    val first = substring(0, 1).uppercase()
    val rest  = substring(1).lowercase()
    return first + rest
}

@Serializable private data class MunicipiosApiResponse(val data: List<MunicipioDto>)
@Serializable private data class MunicipioDto(
    @SerialName("CCOM") val ccaaID: Long,
    @SerialName("CPRO") val provinciaID: Long,
    @SerialName("CMUM") val municipioID: Long,
    @SerialName("DMUN50") val nombre: String
)

@Serializable private data class ProvinciasApiResponse(val data: List<ProvinciaDto>)
@Serializable private data class ProvinciaDto(
    @SerialName("CPRO") val codigo: Long,
    @SerialName("PRO")  val nombre: String
)

interface MunicipioRepository {
    suspend fun searchMunicipios(query: String): List<MunicipioSearchResult>
}

class ApiMunicipioRepository(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val apiKey: String,
    private val version: String = "2025.01",
    private val json: Json = Json { ignoreUnknownKeys = true }
) : MunicipioRepository {

    private val provinciaCache = mutableMapOf<Long, String>()

    override suspend fun searchMunicipios(query: String): List<MunicipioSearchResult> {

        val munisText = httpClient.get("$baseUrl/municipios") {
            parameter("KEY", apiKey)
            parameter("QUERY", query)
            parameter("TYPE", "JSON")
            parameter("VERSION", version)
            parameter("PAGE_SIZE", 1000)
        }.bodyAsText()

        val municipios = json.decodeFromString(
            MunicipiosApiResponse.serializer(), munisText
        ).data

        if (municipios.isEmpty()) return emptyList()

        val provincias = fetchProvincias(municipios.map { it.provinciaID }.toSet())

        return municipios.map { dto ->
            MunicipioSearchResult(
                comunidad_autonoma_ID = dto.ccaaID,
                provincia_ID          = dto.provinciaID,
                municipio_ID          = dto.municipioID,
                municipio             = dto.nombre.toSentenceCase(),
                provincia             = provincias[dto.provinciaID].orEmpty()
            )
        }
    }

    private suspend fun fetchProvincias(codigos: Set<Long>): Map<Long, String> {
        val resultado = mutableMapOf<Long, String>()

        for (cpro in codigos) {
            val cached = provinciaCache[cpro]
            if (cached != null) {
                resultado[cpro] = cached
                continue
            }

            val respText = httpClient.get("$baseUrl/provincias") {
                parameter("CPRO", cpro.toString().padStart(2, '0'))
                parameter("KEY", apiKey)
                parameter("TYPE", "JSON")
                parameter("VERSION", version)
            }.bodyAsText()

            val nombre = json
                .decodeFromString(ProvinciasApiResponse.serializer(), respText)
                .data.firstOrNull()
                ?.nombre
                ?.toSentenceCase()
                .orEmpty()

            provinciaCache[cpro] = nombre
            resultado[cpro]      = nombre
        }
        return resultado
    }
}