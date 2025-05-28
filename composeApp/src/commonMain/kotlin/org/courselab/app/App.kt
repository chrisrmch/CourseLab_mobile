package org.courselab.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.courselab.app.ui.screens.sign_in.LoginScreen
import org.courselab.app.ui.screens.sign_up.SignUpScreen
import org.courselab.app.ui.theme.CourseLabAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Serializable
object LogInScreen

@Serializable
object SignUpScreen


@Preview
@Composable
fun App(logo: Painter?) {
    CourseLabAppTheme(

        content = @Composable {
            KoinContext {
                val navController: NavHostController = rememberNavController()
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
                            },
                            onNavigateToLogin = { navController.popBackStack() }
                        )
                    }
                }
            }

        }
    )
}