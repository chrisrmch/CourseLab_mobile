package org.courselab.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.logo
import courselab.composeapp.generated.resources.logo_dark
import kotlinx.coroutines.flow.first
import org.courselab.app.Screen
import org.courselab.app.data.UserPreferencesDataStore
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SplashRoute(
    prefs: UserPreferencesDataStore,
    navController: NavHostController
) {

    LaunchedEffect(Unit) {
        val logged = prefs.isLoggedIn.first() ?: false
        val firstLog = prefs.isFirstLogin.first() ?: false

        val dest = when {
            !logged -> Screen.LogInScreen
            firstLog -> Screen.FirstOnboardingScreen
            else -> Screen.LogInScreen
        }

        navController.popBackStack(Screen.SplashScreen, inclusive = true)
        navController.navigate(dest) { launchSingleTop = true }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(Res.drawable.logo_dark),
            contentDescription = stringResource(Res.string.logo),
            modifier = Modifier.fillMaxWidth()
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(Res.drawable.logo_dark),
            contentDescription = stringResource(Res.string.logo),
            alignment = Alignment.Center
        )
    }
}