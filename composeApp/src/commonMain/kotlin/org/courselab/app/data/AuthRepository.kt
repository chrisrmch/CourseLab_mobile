package org.courselab.app.data


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import org.courselab.app.ui.screens.sign_up.SignUpRequestDTO

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val accountRole: String = "ROLE_USER",
)

@Serializable
data class LogInResponse(
    val id: Int,
    val name: String?,
    val lastname: String?,
    val email: String,
    val role: String,
    val token: String,
    val type: String,
    val firstLogin: Boolean
)

@Serializable
data class SignUpResponse(
    val message: String,
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
)

class AuthRepository(
    private val client: HttpClient,
    private val baseUrl: String,
) {
    suspend fun logIn(email: String, password: String): ApiResponse<LogInResponse> {
        try {
            val response: HttpResponse = client.post("$baseUrl/auth/signin") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }

            println("Request URL: ${response.request.url}")
            println("Response Status: ${response.status}")
            println("Response Body: ${response.body<String>()}")

            if (response.status.value == 200 && response.body<LogInResponse>().firstLogin) {
                val data = ApiResponse(
                    success = true,
                    data = response.body<LogInResponse>(),
                    message = "Bienvenido a CourseLab, por favor completa tu perfil"
                )
                return data

            } else if (response.status.value == 200) {
                val data = ApiResponse(
                    success = true,
                    data = response.body<LogInResponse>(),
                    message = "Sesión iniciada correctamente"
                )
                return data
            } else if (response.status.value == 406) {
                println(response.status.description)
                val errorMessage = response.body<String>()
                println("Login failed with status code ${response.status.value}")
                return ApiResponse(
                    success = false,
                    data = null,
                    message = errorMessage
                )
            } else if (response.status.value == 417) {
                println(response.status.description)
                println("Login failed with status code ${response.status.value}")
                return ApiResponse(
                    success = false,
                    data = null,
                    message = "El correo o la contraseña son incorrectos"
                )
            } else {
                println("Login failed with status code ${response.status.value}")
                return ApiResponse(
                    success = false,
                    data = null,
                    message = "No se ha podido iniciar sesión"
                )
            }
        } catch (e: Exception) {
            println("An error occurred during login: ${e.message}")
            return ApiResponse(
                success = false,
                data = null,
                message = "Ups... ha ocurrido un error inesperado :("
            )
        }
    }

    suspend fun signUp(signUpRequest: SignUpRequestDTO): ApiResponse<SignUpResponse> {
        return try {
            val response: HttpResponse = client.post("$baseUrl/auth/signup/user") {
                contentType(ContentType.Application.Json)
                setBody(signUpRequest)
            }
            println("Request URL: ${response.request.url}")
            println("Response Status: ${response.status}")
            val bodyText = response.body<String>()
            println("Response Body: $bodyText")
            when (response.status.value) {
                200 -> {
                    ApiResponse(
                        success = true,
                        data = null,
                        message = "Sólo te queda aceptar el correo!"
                    )
                }

                400 -> {
                    ApiResponse(
                        success = false,
                        data = null,
                        message = "Este correo ya está registrado"
                    )
                }

                else -> {
                    ApiResponse(
                        success = false,
                        data = null,
                        message = "No se ha podido crear la cuenta :("
                    )
                }
            }
        } catch (e: Exception) {
            println("An error occurred during signup: ${e.message}")
            ApiResponse(
                success = false,
                data = null,
                message = "Ups... ha ocurrido un error inesperado"
            )
        }
    }

    /**
     * Extrae el host de la baseUrl (por ejemplo "http://192.168.1.5:8081" → "192.168.1.5").
     * Si tu baseUrl es algo como "http://192.168.1.5:8081", devolvemos solo el hostname.
     */
    private fun extractHost(baseUrl: String): String {
        // Asumimos "http[s]://host:puerto", simplemente tomamos el substring entre "://" y ":"
        return baseUrl
            .substringAfter("://")
            .substringBefore(":")
    }

    /**
     * Extrae el puerto de la baseUrl, p.e. "http://192.168.1.5:8081" → 8081.
     * Si no hay puerto explícito, devolvemos 80 o 443 según el esquema.
     */
    private fun extractPort(baseUrl: String): Int {
        val afterProtocol = baseUrl.substringAfter("://")
        return if (afterProtocol.contains(":")) {
            afterProtocol.substringAfter(":").toIntOrNull() ?: 80
        } else {
            if (baseUrl.startsWith("https")) 443 else 80
        }
    }
}