package org.courselab.app.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.data.domain.GetMunicipiosUseCase
import org.courselab.app.data.repository.ActivityRepository
import org.courselab.app.data.repository.ApiMunicipioRepository
import org.courselab.app.data.repository.AuthRepository
import org.courselab.app.data.repository.MunicipioRepository
import org.courselab.app.data.repository.UserRepository
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    }, contentType = ContentType.Application.Any
                )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP-CLIENT ➜ $message")
                    }
                }
                level = LogLevel.ALL
                sanitizeHeader { false }
            }
        }
    }
}


val repositoriesModules = module {
    val serverURL = "http://192.168.1.12:8081"
    val geoapiURL = "https://apiv1.geoapi.es"
    val API_KEY = "84b693743141c621ecf6e6c2e00c345067972b320ac2c95263dae0caa8f34e38"
    single { UserPreferencesDataStore(get()) }
    single { AuthRepository(get(), serverURL, get()) }
    single { UserRepository(get(), serverURL, get()) }
    single<MunicipioRepository> { ApiMunicipioRepository(get(), geoapiURL, API_KEY) }
    single { GetMunicipiosUseCase(get()) }
    single { ActivityRepository(get(), serverURL, get()) }
}
