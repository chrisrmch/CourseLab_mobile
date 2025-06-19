package org.courselab.app.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.models.ActividadDTO
import org.courselab.app.models.ActividadDTORequest


class ActivityRepository(
    private val client: HttpClient,
    private val serverURL: String,
    private val userPrefs: UserPreferencesDataStore
) {
    suspend fun uploadActividad(activity: ActividadDTORequest): Boolean {
        val token = userPrefs.sessionToken.first()
            ?: throw IllegalStateException("No hay token en UserPreferencesDataStore")

        println("Token: $token")
        println("id: $activity")

        val response = client.post("$serverURL/activity/register") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(activity)
        }

        return response.status == HttpStatusCode.Created ||
                response.status == HttpStatusCode.OK
    }


    suspend fun getAllUserActivities(): List<ActividadDTO> {
        val token = userPrefs.sessionToken.first()
            ?: throw IllegalStateException("No hay token en UserPreferencesDataStore")
        val userId = userPrefs.userId.first()
            ?: throw IllegalStateException("No hay userId en UserPreferencesDataStore")

        val response: HttpResponse = client.get("$serverURL/activity/user/$userId") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                response.body()
            }

            HttpStatusCode.Unauthorized -> {
                throw IllegalStateException("No autorizado. Token inválido o expirado.")
            }

            else -> {
                emptyList()
            }
        }

    }
}