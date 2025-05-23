package org.courselab.app

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import io.ktor.client.HttpClient
import org.courselab.app.ui.screens.WelcomeScreen
import org.courselab.app.ui.screens.SignUpScreen
import org.courselab.app.ui.theme.AppTheme
import org.courselab.app.viewmodel.AuthViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

expect fun createHttpClient(): HttpClient

@Preview
@Composable
fun App(
    logo: Painter? = null,
    httpClient : HttpClient
) {
    val authViewModel = remember { AuthViewModel(
        httpClient = httpClient,
        baseUrl = "http://192.168.1.7:8081"
    ) }
    var currentScreen by remember { mutableStateOf("welcome") }

    AppTheme {
        when (currentScreen) {
            "welcome" -> WelcomeScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { /* ir a Home */ },
                onSignUpNavigate = { currentScreen = "signup" },
                logo = logo
            )

            "signup" -> SignUpScreen(
                authViewModel = authViewModel,
                onSignUpComplete = { success -> if (success) currentScreen = "welcome" },
                logo = logo
            )
        }
    }
}