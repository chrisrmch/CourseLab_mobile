package org.courselab.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.ui.screens.onboarding.OnboardingStep2
import org.courselab.app.ui.screens.onboarding.UserInformationStep
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

@Serializable
object FirstOnboardingScreen

@Serializable
object SecondOnboardingScreen

val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }

val LocalAppLocalization = compositionLocalOf {
    AppLang.Spanish
}

val LocalUrlLauncher = compositionLocalOf<UrlLauncher?> { null }

@Preview
@Composable
fun App(
    logo: Painter, userPreferences: UserPreferencesDataStore = koinInject(),
) {
    val currentThemePreference by userPreferences.themePreference.collectAsState(initial = "system")
    val useDarkTheme = when (currentThemePreference) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    val currentLanguage = rememberAppLocale()
    val urlLauncher = rememberUrlLauncher()

    CourseLabAppTheme(
        darkTheme = useDarkTheme,
        content = @Composable {
            KoinContext {
                val navController: NavHostController = rememberNavController()
                CompositionLocalProvider(
                    LocalNavController provides navController,
                    LocalAppLocalization provides currentLanguage,
                    LocalUrlLauncher provides urlLauncher
                ) {
                    NavHost(
                        navController = navController,
                        enterTransition = {
                            SlideInLeftAnimation()
                        },
                        exitTransition = {
                            SlideOutLeftAnimation()
                        },
                        popEnterTransition = {
                            SlideInRightAnimation()
                        },
                        popExitTransition = {
                            SlideOutRightAnimation()
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
                            UserInformationStep(
                                logo = logo,
                                onNext = {
                                    navController.navigate(SecondOnboardingScreen)
                                },
                            );
                        }
                        composable<FirstOnboardingScreen> {
                            UserInformationStep(
                                logo = logo,
                                onNext = {
                                    navController.navigate(SecondOnboardingScreen)
                                },
                            )
                        }
                        composable<SecondOnboardingScreen> {
                            OnboardingStep2(
                                logo = logo,
                            )
                        }
                        composable<HomeScreen> {
//                            HomeScreen()
                        }
                    }
                }
            }

        }
    )
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.SlideOutRightAnimation() =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(500)
    )

private fun AnimatedContentTransitionScope<NavBackStackEntry>.SlideInRightAnimation() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(500)
    )

private fun AnimatedContentTransitionScope<NavBackStackEntry>.SlideOutLeftAnimation() =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(500)
    )

private fun AnimatedContentTransitionScope<NavBackStackEntry>.SlideInLeftAnimation() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(500)
    )