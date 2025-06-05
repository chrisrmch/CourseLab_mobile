package org.courselab.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.ui.screens.onboarding.OnboardingStep1
import org.courselab.app.ui.screens.onboarding.OnboardingStep2
import org.courselab.app.ui.screens.sign_in.LoginScreen
import org.courselab.app.ui.theme.CourseLabAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Serializable
object LogInScreen

@Serializable
object SignUpScreen

@Serializable
object HomeScreen

val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }

@Preview
@Composable
fun App(logo: Painter?, userPreferences: UserPreferencesDataStore = koinInject()) {
    val currentThemePreference by userPreferences.themePreference.collectAsState(initial = "system")
    val useDarkTheme = when (currentThemePreference) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    CourseLabAppTheme(
        darkTheme = useDarkTheme,
        content = @Composable {
            KoinContext {
                val navController: NavHostController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(
                        navController = navController,
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(500)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(500)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(500)
                            )
                        },
                        startDestination = LogInScreen
                    ) {
                        composable<LogInScreen> {
                            LoginScreen(
                                onLoginSuccess = {
                                    println("LOGIN SUCCESSFUL, NAVIGATING TO HOME")
                                    navController.navigate(HomeScreen)
                                },
                                onSignUpNavigate = { navController.navigate(SignUpScreen) },
                                logo = logo
                            )
                        }
                        composable<SignUpScreen> {
                            OnboardingStep1(
                                logo = logo,
                                onNext = { nombre, apellidos, fechaNacimiento, genero ->
                                    navController.navigate(HomeScreen)
                                },
                                onBackToLogin = { navController.popBackStack() }
                            );
//                            SignUpScreen(
//                                logo = logo,
//                                onSignUpComplete = { success ->
//                                    if (success) navController.navigate(
//                                        LogInScreen
//                                    )
//                                },
//                                onNavigateToLogin = { navController.popBackStack() }
//                            )
                        }
                        composable<HomeScreen> {
                            OnboardingStep1(
                                logo = logo,
                                onNext = { nombre: String, apellidos: String, fechaNacimiento: LocalDate, genero: String ->
                                    OnboardingStep2(
                                        initialNombre = nombre,
                                        initialApellidos = apellidos,
                                        initialFechaNacimiento = fechaNacimiento,
                                        initialGenero = genero,
                                        onBack = {   },
                                    ){ fotoPerfilUri: String?, biografia: String, enlaceWeb: String, ubicacion: String, intereses: List<String> ->

                                    }
                                },
                                onBackToLogin = {

                                }
                            )
                        }
                    }
                }
            }

        }
    )
}