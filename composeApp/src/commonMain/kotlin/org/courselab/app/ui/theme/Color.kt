package org.courselab.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * A color scheme holds all the named color parameters for a [MaterialTheme].
 *
 * Color schemes are designed to be harmonious, ensure accessible text, and distinguish UI elements
 * and surfaces from one another. There are two built-in baseline schemes, [lightColorScheme] and a
 * [darkColorScheme], that can be used as-is or customized.
 *
 * The Material color system and custom schemes provide default values for color as a starting point
 * for customization.
 *
 * Example of extending color scheme, including implementing Fixed Accent colors:
 *
 * @sample androidx.compose.material3.samples.ColorSchemeFixedAccentColorSample
 *
 * To learn more about colors, see
 * [Material Design colors](https://m3.material.io/styles/color/overview).
 *
 * @property primary The primary color is the color displayed most frequently across your app’s
 *   screens and components.
 * @property onPrimary Color used for text and icons displayed on top of the primary color.
 * @property primaryContainer The preferred tonal color of containers.
 * @property onPrimaryContainer The color (and state variants) that should be used for content on
 *   top of [primaryContainer].
 * @property inversePrimary Color to be used as a "primary" color in places where the inverse color
 *   scheme is needed, such as the button on a SnackBar.
 * @property secondary The secondary color provides more ways to accent and distinguish your
 *   product. Secondary colors are best for:
 * - Floating action buttons
 * - Selection controls, like checkboxes and radio buttons
 * - Highlighting selected text
 * - Links and headlines
 *
 * @property onSecondary Color used for text and icons displayed on top of the secondary color.
 * @property secondaryContainer A tonal color to be used in containers.
 * @property onSecondaryContainer The color (and state variants) that should be used for content on
 *   top of [secondaryContainer].
 * @property tertiary The tertiary color that can be used to balance primary and secondary colors,
 *   or bring heightened attention to an element such as an input field.
 * @property onTertiary Color used for text and icons displayed on top of the tertiary color.
 * @property tertiaryContainer A tonal color to be used in containers.
 * @property onTertiaryContainer The color (and state variants) that should be used for content on
 *   top of [tertiaryContainer].
 * @property background The background color that appears behind scrollable content.
 * @property onBackground Color used for text and icons displayed on top of the background color.
 * @property surface The surface color that affect surfaces of components, such as cards, sheets,
 *   and menus.
 * @property onSurface Color used for text and icons displayed on top of the surface color.
 * @property surfaceVariant Another option for a color with similar uses of [surface].
 * @property onSurfaceVariant The color (and state variants) that can be used for content on top of
 *   [surface].
 * @property surfaceTint This color will be used by components that apply tonal elevation and is
 *   applied on top of [surface]. The higher the elevation the more this color is used.
 * @property inverseSurface A color that contrasts sharply with [surface]. Useful for surfaces that
 *   sit on top of other surfaces with [surface] color.
 * @property inverseOnSurface A color that contrasts well with [inverseSurface]. Useful for content
 *   that sits on top of containers that are [inverseSurface].
 * @property error The error color is used to indicate errors in components, such as invalid text in
 *   a text field.
 * @property onError Color used for text and icons displayed on top of the error color.
 * @property errorContainer The preferred tonal color of error containers.
 * @property onErrorContainer The color (and state variants) that should be used for content on top
 *   of [errorContainer].
 * @property outline Subtle color used for boundaries. Outline color role adds contrast for
 *   accessibility purposes.
 * @property outlineVariant Utility color used for boundaries for decorative elements when strong
 *   contrast is not required.
 * @property scrim Color of a scrim that obscures content.
 * @property surfaceBright A [surface] variant that is always brighter than [surface], whether in
 *   light or dark mode.
 * @property surfaceDim A [surface] variant that is always dimmer than [surface], whether in light
 *   or dark mode.
 * @property surfaceContainer A [surface] variant that affects containers of components, such as
 *   cards, sheets, and menus.
 * @property surfaceContainerHigh A [surface] variant for containers with higher emphasis than
 *   [surfaceContainer]. Use this role for content which requires more emphasis than
 *   [surfaceContainer].
 * @property surfaceContainerHighest A [surface] variant for containers with higher emphasis than
 *   [surfaceContainerHigh]. Use this role for content which requires more emphasis than
 *   [surfaceContainerHigh].
 * @property surfaceContainerLow A [surface] variant for containers with lower emphasis than
 *   [surfaceContainer]. Use this role for content which requires less emphasis than
 *   [surfaceContainer].
 * @property surfaceContainerLowest A [surface] variant for containers with lower emphasis than
 *   [surfaceContainerLow]. Use this role for content which requires less emphasis than
 *   [surfaceContainerLow]. Para el Scaffold, generalmente se utiliza `background` o `surface`
 *   dependiendo del diseño que se quiera lograr.
 */

val darkColours = darkColorScheme(
    primary =  Color(red = 32, green = 32, blue = 32),
    onPrimary = Color(red = 235, green =235, blue = 235),
    primaryContainer = Color(color = 0xFFFF9800),
    onPrimaryContainer = Color(color = 0xff000000),
    inversePrimary = Color(color = 0xFFFFFFFF),

    secondary =  Color(color = 0xFFFF9800),
    onSecondary = Color(color = 0xff000000),
    secondaryContainer = Color(color = 0xFFFFE0B2),
    onSecondaryContainer = Color(color = 0xFFE65100),

    tertiary = Color(color = 0xFF388E3C),
    onTertiary =  Color(color = 0xcbc3c3c3),
    tertiaryContainer =  Color(color = 0xFFC8E6C9),
    onTertiaryContainer = Color(color = 0xff225d24),

    background = Color(red = 69, green = 68, blue = 64),
    onBackground = Color(color = 0xFFFFFFFF),
    surface = Color(red = 69, green = 68, blue = 64),
    onSurface = Color(red = 230, green = 230, blue = 230),
    surfaceVariant = Color(red = 52, green = 42, blue = 14),
    onSurfaceVariant =  Color(red = 92, green = 89, blue = 84),
    surfaceTint =  Color(red = 173, green = 173, blue = 173),


    inverseSurface = Color(color = 0xFFEEEEEE),
    inverseOnSurface = Color(color = 0xFF121212),


    error =  Color(color = 0xFFD32F2F),
    onError = Color(color = 0xffff0034),
    errorContainer =  Color(color = 0xFFFFCDD2),
    onErrorContainer = Color(color = 0xFF8E0000),

    outline = Color(color = 0xFF707070),
    outlineVariant = Color(color = 0xFFD1C4E9),
    scrim = Color(color = 0x99000000),
    surfaceBright = Color(color = 0xFFFFFFFF),
    surfaceDim = Color(color = 0xFFF5F5F5),
    surfaceContainer = Color(color = 0xFFF0F0F0),
    surfaceContainerHigh = Color(color = 0xFFE0E0E0),
    surfaceContainerHighest =Color(color = 0xff1f1c1c),
    surfaceContainerLow = Color(color = 0xff2f2c2c),
    surfaceContainerLowest =  Color(color = 0xffffffff)
)

val lightColours = lightColorScheme(
    primary = Color(red = 227, green = 183, blue = 9),
    onPrimary = Color(color = 0xFFFFFFFF),
    primaryContainer = Color(red = 242, green = 242, blue = 242),
    onPrimaryContainer = Color(red = 110, green = 113, blue = 145),
    inversePrimary = Color(red = 78, green = 75, blue = 102),

    secondary = Color(red = 252, green = 252, blue = 252),
    onSecondary = Color(red = 20, green = 20, blue = 42),
    secondaryContainer = Color(red = 254, green = 236, blue = 186),
    onSecondaryContainer = Color(red = 32, green = 32, blue = 32),

    tertiary = Color(red = 239, green = 236, blue = 254),
    onTertiary = Color(red = 69, green = 68, blue = 64),
    tertiaryContainer =  Color(red = 214, green = 208, blue = 253),
    onTertiaryContainer = Color(red = 20, green = 20, blue = 42),

    background = Color(red = 245, green = 245, blue = 245),
    onBackground = Color(red = 143, green = 143, blue = 143),
    surface = Color(red = 229, green = 229, blue = 229),
    onSurface = Color(red = 102, green = 104, blue = 106),
    surfaceVariant = Color(red = 248, green = 248, blue = 252),
    onSurfaceVariant = Color(red = 164, green = 164, blue = 163),
    surfaceTint = Color(red = 50, green = 46, blue = 29),
    inverseSurface = Color(color = 0xff000000),
    inverseOnSurface =  Color(color = 0xcbc3c3c3),

    error =  Color(color = 0xFFFFCDD2),
    onError = Color(color = 0xFF8E0000),
    errorContainer = Color(color = 0xFF8E0000),
    onErrorContainer =  Color(color = 0xFFFFCDD2),

    outline = Color(color = 0xFFBBBBBB),
    outlineVariant = Color(color = 0xFF757575),
    scrim = Color(color = 0x99000000),
    surfaceBright = Color(color = 0xFF2A2A2A),
    surfaceDim = Color(color = 0xFF181818),
    surfaceContainer = Color(color = 0xFF222222),
    surfaceContainerHigh = Color(color = 0xFF2E2E2E),
    surfaceContainerHighest = Color(color = 0x7affeca3),
    surfaceContainerLow = Color(color = 0x52fff29f),
    surfaceContainerLowest = Color(color = 0xFF1C1C1C)
)
