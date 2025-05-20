package org.courselab.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = YellowPrimary,
    onPrimary = BlackPrimary,
    primaryContainer = YellowLight,
    onPrimaryContainer = BlackPrimary,
    secondary = Rose,
    onSecondary = BlackPrimary,
    background = BlackPrimary,
    onBackground = YellowLight,
    surface = BlackPrimary,
    onSurface = YellowLight,
    error = RedPrimary,
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = YellowLight,
    onPrimary = BlackPrimary,
    primaryContainer = YellowPrimary,
    onPrimaryContainer = BlackPrimary,
    secondary = Rose,
    onSecondary = BlackPrimary,
    background = BlackPrimary,
    onBackground = YellowLight,
    surface = BlackPrimary,
    onSurface = YellowLight,
    error = RedLight,
    onError = Color.White
)

val appTypography = Typography()

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) DarkColors else LightColors,
        typography = appTypography,
        content = content
    )
}
