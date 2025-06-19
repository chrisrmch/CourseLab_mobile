package org.courselab.app.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.courselab.app.models.PerfilUsuarioResponseDTO
import java.io.File

private val JSON  = "application/json; charset=utf-8".toMediaType()
private val JPEG  = "image/jpeg".toMediaType()
private val FORM  = MultipartBody.FORM

//
//private fun MultipartBody.Builder.addTextPart(name: String, value: String) =
//    addPart(
//        Headers.headersOf("Content-Disposition", """form-data; name="$name""""),
//        value.toRequestBody(TEXT_PLAIN)
//    )
//

private val json = Json { ignoreUnknownKeys = true }

actual suspend fun uploadUserProfile(
    baseUrl: String,
    token: String,
    perfilUsuarioRequestDTO: PerfilUsuarioRequestDTO,
    fotoBytes: ByteArray?
): PerfilUsuarioResponseDTO = withContext(Dispatchers.IO) {

    val perfilJson = Json.encodeToString(PerfilUsuarioRequestDTO.serializer(), perfilUsuarioRequestDTO)

    val multipartBody = MultipartBody.Builder().setType(FORM)
        .addFormDataPart(
            "perfil",
            "perfil.json",
            perfilJson.toRequestBody(JSON)
        )
        .apply {
            fotoBytes?.let { bytes ->
                val tmp = File.createTempFile("avatar_", ".jpg").apply { writeBytes(bytes) }
                addFormDataPart(
                    "fotoPerfil",
                    tmp.name,
                    tmp.asRequestBody(JPEG))
            }
        }
        .build()

    val request = Request.Builder()
        .url("$baseUrl/perfilUsuario")
        .addHeader("Authorization", "Bearer $token")
        .post(multipartBody)
        .build()

    println("HTTP ➜ REQUEST  : ${request.method} ${request.url}")
    request.headers.forEach { println("HTTP ➜ HEADER   : ${it.first}: ${it.second}") }

    val client   = OkHttpClient()
    val response = client.newCall(request).execute()

    println("HTTP ➜ RESPONSE : ${response.code}")

    if (!response.isSuccessful) {
        val msg = response.body?.string() ?: "<no body>"
        throw IllegalStateException("Upload failed ${response.code}: $msg")
    }

    val rawJson = response.body!!.string()
    println("JSON RECEIVED   : $rawJson")

    json
        .decodeFromString(PerfilUsuarioResponseDTO.serializer(), rawJson)
}