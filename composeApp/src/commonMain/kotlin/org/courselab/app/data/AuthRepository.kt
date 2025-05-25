package org.courselab.app.data


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class SignUpRequest(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val password: String,
    val fechaNacimiento: String,
    val genero: String
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)

class AuthRepository(
    private val client: HttpClient,
    private val baseUrl: String
) {
    suspend fun logIn(email: String, password: String): ApiResponse<Unit> {
        print("se ha llegado aqui")
        return client.post("$baseUrl/auth/signin") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()
    }

    suspend fun signUp(request: SignUpRequest): ApiResponse<Unit> =
        client.post("$baseUrl/auth/signup/user") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}