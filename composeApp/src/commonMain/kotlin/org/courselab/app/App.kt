
package org.courselab.app.org.courselab.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.logo_dark
import courselab.composeapp.generated.resources.logo_light
import kotlinx.serialization.Serializable
import org.courselab.app.AppLang
import org.courselab.app.UrlLauncher
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.rememberAppLocale
import org.courselab.app.rememberUrlLauncher
import org.courselab.app.ui.screens.home.HomeScreen
import org.courselab.app.ui.screens.onboarding.OnboardingStep2
import org.courselab.app.ui.screens.onboarding.UserInformationStep
import org.courselab.app.ui.screens.log_in.LoginScreen
import org.courselab.app.ui.theme.CourseLabAppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Serializable
object LogInScreen

@Serializable
object SignUpScreen

@Serializable
object OnBoarding

@Serializable
object HomeScreen

@Serializable
object FirstOnboardingScreen

@Serializable
object SecondOnboardingScreen

val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }

val LocalAppLocalization = compositionLocalOf { AppLang.Spanish }

val LocalUrlLauncher = compositionLocalOf<UrlLauncher?> { null }

// Lanza la solicitud de permiso; hace *nothing* por defecto
val LocalRequestLocationPermission =
    staticCompositionLocalOf<() -> Unit> { {  } }


// true si el permiso está concedido; false por defecto
val LocalLocationPermissionGranted =
    compositionLocalOf { false }

@Preview
@Composable
fun App(
    userPreferences: UserPreferencesDataStore = koinInject(),
) {
    val currentThemePreference by userPreferences.themePreference.collectAsState(initial = "system")
    val useDarkTheme = when (currentThemePreference) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }
    val currentLanguage = rememberAppLocale()
    val urlLauncher = rememberUrlLauncher()
    val logo = if(useDarkTheme)  painterResource(Res.drawable.logo_dark) else painterResource(Res.drawable.logo_light)
    CourseLabAppTheme(
        darkTheme = useDarkTheme,
        content = @Composable {
            KoinContext {
                val navController: NavHostController = rememberNavController()
                CompositionLocalProvider(
                    LocalNavController provides navController,
                    LocalAppLocalization provides currentLanguage,
                    LocalUrlLauncher provides urlLauncher,
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
                        startDestination = HomeScreen
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
                            HomeScreen()
                        }
                    }
                }
            }
            StatusBarProtection()
        }
    )
}


@Composable
private fun StatusBarProtection(
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
    heightProvider: () -> Float = calculateGradientHeight(),
) {

    Canvas(Modifier.fillMaxSize()) {
        val calculatedHeight = heightProvider()
        val gradient = Brush.verticalGradient(
            colors = listOf(
                color.copy(alpha = 1f),
                color.copy(alpha = .8f),
                Color.Transparent
            ),
            startY = 0f,
            endY = calculatedHeight
        )
        drawRect(
            brush = gradient,
            size = Size(size.width, calculatedHeight),
        )
    }
}

@Composable
fun calculateGradientHeight(): () -> Float {
    val statusBars = WindowInsets.statusBars
    val density = LocalDensity.current
    return { statusBars.getTop(density).times(1.2f) }
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