package org.courselab.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.logo_dark
import courselab.composeapp.generated.resources.logo_light
import kotlinx.serialization.Serializable
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.ui.screens.SplashRoute
import org.courselab.app.ui.screens.homeNavigation.HomeScreenNavigationHolder
import org.courselab.app.ui.screens.onboarding.UserProfileDetailsScreenFirst
import org.courselab.app.ui.screens.onboarding.UserProfileDetailsScreenSecond
import org.courselab.app.ui.screens.sign_in.LoginScreen
import org.courselab.app.ui.screens.sign_up.SignUpScreen
import org.courselab.app.ui.theme.CourseLabAppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Serializable
sealed interface Screen {
    @Serializable
    data object SplashScreen : Screen
    @Serializable
    data object LogInScreen : Screen
    @Serializable
    data object SignUpScreen : Screen
    @Serializable
    data object HomeScreen : Screen
    @Serializable
    data object FirstOnboardingScreen : Screen
    @Serializable
    data object SecondOnboardingScreen : Screen
}


val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }

val LocalAppLocalization = compositionLocalOf { AppLang.Spanish }

val LocalUrlLauncher = compositionLocalOf<UrlLauncher?> { null }

val LocalRequestLocationPermission = staticCompositionLocalOf<() -> Unit> { { } }

val LocalLocationPermissionGranted = compositionLocalOf { false }

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
    val logo =
        if (useDarkTheme) painterResource(Res.drawable.logo_dark) else painterResource(Res.drawable.logo_light)
    val navController: NavHostController = rememberNavController()

    CourseLabAppTheme(
        darkTheme = useDarkTheme, content = @Composable {
            KoinContext {
                CompositionLocalProvider(
                    LocalNavController provides navController,
                    LocalAppLocalization provides currentLanguage,
                    LocalUrlLauncher provides urlLauncher,
                ) {
                    NavHost(
                        navController = navController, enterTransition = {
                            SlideInLeftAnimation()
                        }, exitTransition = {
                            SlideOutLeftAnimation()
                        }, popEnterTransition = {
                            SlideInRightAnimation()
                        }, popExitTransition = {
                            SlideOutRightAnimation()
                        }, startDestination = Screen.SplashScreen
                    ) {
                        composable<Screen.SplashScreen> {
                            SplashRoute(userPreferences, navController)
                        }
                        composable<Screen.LogInScreen> {
                            LoginScreen(
                                logo = logo,
                                onSignUpNavigate = { navController.navigate(Screen.SignUpScreen) }
                            )
                        }
                        composable<Screen.SignUpScreen> {
                            SignUpScreen(
                                logo = logo,
                            )
                        }
                        composable<Screen.FirstOnboardingScreen> {
                            UserProfileDetailsScreenFirst(
                                logo = logo,
                                onNext = {
                                    navController.navigate(Screen.SecondOnboardingScreen)
                                },
                            )
                        }
                        composable<Screen.SecondOnboardingScreen> {
                            UserProfileDetailsScreenSecond(
                                logo = logo,
                            )
                        }
                        composable<Screen.HomeScreen> {
                            HomeScreenNavigationHolder()
                        }
                    }
                }
            }
            StatusBarProtection()
        })
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
                color.copy(alpha = 1f), color.copy(alpha = .8f), Color.Transparent
            ), startY = 0f, endY = calculatedHeight
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
        AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
    )

private fun AnimatedContentTransitionScope<NavBackStackEntry>.SlideInRightAnimation() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500)
    )

private fun AnimatedContentTransitionScope<NavBackStackEntry>.SlideOutLeftAnimation() =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
    )

private fun AnimatedContentTransitionScope<NavBackStackEntry>.SlideInLeftAnimation() =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500)
    )