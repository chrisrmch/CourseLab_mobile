package org.courselab.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CourseLabAppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) {
        darkColorScheme(
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
    } else {
        lightColorScheme(
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
    }

    val typography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content,
    )
}
