package org.courselab.app.data


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.Serializable
import org.courselab.app.ui.screens.sign_up.SignUp

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val accountRole: String = "ROLE_USER",
)

@Serializable
data class LogInResponse(
    val id: Int,
    val name: String,
    val lastname: String,
    val email: String,
    val role: String,
    val token: String,
    val type: String = "Bearer",
//    "id": 1,
//    "name": "Christian",
//    "lastname": "Mamani",
//    "email": "christianrmch@outlook.es",
//    "role": "ROLE_USER",
//    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaHJpc3RpYW5ybWNoQG91dGxvb2suZXMiLCJpYXQiOjE3NDg1MzI5MzYsImV4cCI6MTc0ODYxOTMzNn0.2PwctuV4_8nhuSAeXzeBaL5V3tRJOQNBZ5NAMztRhxw",
//    "type": "Bearer"
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
            if (response.status.value == 200) {
                val data = ApiResponse(
                    success = true,
                    data = response.body<LogInResponse>(),
                    message = null
                )
                return data;
            } else {
                println("Login failed with status code ${response.status.value}")
                return ApiResponse(
                    success = false,
                    data = null,
                    message = "login failed with status code ${response.status.value}"
                )
            }
        } catch (e: Exception) {
            println("An error occurred during login: ${e.message}")
            return ApiResponse(
                success = false,
                data = null,
                message = "login failed due to an exception: ${e.message}"
            )
        }
    }

    suspend fun signUp(signUpRequest: SignUp): ApiResponse<Unit> {
        try {
            val response: HttpResponse = client.post("$baseUrl/auth/signup/user") {
                contentType(ContentType.Application.Json)
                setBody(signUpRequest)
            }
            println("Request URL: ${response.request.url}")
            println("Response Status: ${response.status}")
            println("Response Body: ${response.body<String>()}")
            if (response.status.value == 200) {
                return response.body<ApiResponse<Unit>>()
            } else {
                println("Login failed with status code ${response.status.value}")
                return ApiResponse(
                    success = false,
                    data = null,
                    message = "login failed with status code ${response.status.value}"
                )
            }
        } catch (e: Exception) {
            println("An error occurred during login: ${e.message}")
            return ApiResponse(
                success = false,
                data = null,
                message = "login failed due to an exception: ${e.message}"
            )
        }
    }
}