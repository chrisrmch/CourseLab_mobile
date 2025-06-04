package org.courselab.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import kotlinx.serialization.Serializable
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.ui.screens.home.HomeScreen
import org.courselab.app.ui.screens.sign_in.LoginScreen
import org.courselab.app.ui.screens.sign_up.SignUpScreen
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
                            SignUpScreen(
                                logo = logo,
                                onSignUpComplete = { success ->
                                    if (success) navController.navigate(
                                        LogInScreen
                                    )
                                },
                                onNavigateToLogin = { navController.popBackStack() }
                            )
                        }
                        composable<HomeScreen> {
                            HomeScreen()
                        }
                    }
                }
            }

        }
    )
}