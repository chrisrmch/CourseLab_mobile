package org.courselab.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.courselab.app.R.drawable.courselab
import kotlin.time.Duration

actual fun createHttpClient(): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            config {
                connectTimeout(
                    Duration.parse("30s").inWholeMilliseconds.toLong(),
                    java.util.concurrent.TimeUnit.MILLISECONDS
                )
                readTimeout(
                    Duration.parse("30s").inWholeMilliseconds.toLong(),
                    java.util.concurrent.TimeUnit.MILLISECONDS
                )
                writeTimeout(
                    Duration.parse("30s").inWholeMilliseconds.toLong(),
                    java.util.concurrent.TimeUnit.MILLISECONDS
                )
            }
        }
        install(ContentNegotiation) {
            json()
        }
    }
}


@Composable
fun AndroidApp() {
    val logoPainter = painterResource(id = courselab)
    App(logo = logoPainter, httpClient = createHttpClient())
}