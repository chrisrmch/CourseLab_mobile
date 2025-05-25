package org.courselab.app

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable
import org.courselab.app.ui.screens.sign_in.LogInViewModel
import org.courselab.app.ui.screens.sign_in.WelcomeScreen
import org.courselab.app.ui.screens.sign_up.SignUpScreen
import org.courselab.app.ui.theme.CourseLabAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

expect fun createHttpClient(): HttpClient

@Serializable
object LogInScreen

@Serializable
object SignUpScreen


@Preview
@Composable
fun App(logo: Painter? = null) {
    CourseLabAppTheme(
        content = @Composable {
            val navController: NavHostController = rememberNavController()
            NavHost(navController = navController, startDestination = LogInScreen) {
                composable<LogInScreen> {
                    WelcomeScreen(
                        loginViewModel = koinInject<LogInViewModel>(),
                        onLoginSuccess = { /* ir a Home */ },
                        onSignUpNavigate = { navController.navigate(SignUpScreen) },
                        logo = logo
                    )
                }
                composable<SignUpScreen> {
                    SignUpScreen(
                        logo = logo,
                        onSignUpComplete = { success ->
                            if (success) navController.navigate(
                                LogInScreen
                            )
                        }
                    )
                }
            }
        }
    )
}