package org.courselab.app.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.courselab.app.data.AuthRepository
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
                    },
                    contentType = ContentType.Application.Json
                )
            }
        }
    }
}

val repositoriesModules = module {
    val serverURL = "http://10.0.2.2:8080"
    single { AuthRepository(get(), serverURL) }
}
