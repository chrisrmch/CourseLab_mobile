package org.courselab.app.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.courselab.app.data.AuthRepository
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.data.UserRepository
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
                    }, contentType = ContentType.Application.Any
                )
            }
//            install(WebSockets) {
//                println("SE HA INICIADO EL WEBSOCKET")
//                extensions {
//                    pingIntervalMillis = 50000L
//                }
//            }
        }
    }
}


val repositoriesModules = module {
    val serverURL = "http://192.168.1.12:8081"
    single { UserPreferencesDataStore(get()) }
    single { AuthRepository(get(), serverURL, get()) }
    single { UserRepository(get(), serverURL, get()) }
}
