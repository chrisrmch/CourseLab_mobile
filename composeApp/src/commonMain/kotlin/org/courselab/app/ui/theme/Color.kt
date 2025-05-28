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

private val EnergyOrangeGenerated = Color(red = 227, green = 183, blue = 9)
private val EnergyOrangeGeneratedInverse = Color(red = 28, green = 72, blue = 246)

private val EnergyOrange = Color(color = 0xFFFF9800)
private val EnergyOrangeLight = Color(color = 0xFFFFE0B2)
private val PureBlack = Color(color = 0xff000000)
private val LightWithe = Color(color = 0xFFF5F5F5)
private val PureWithe = Color(color = 0xFFFFFFFF)
private val DarkGray = Color(red = 69, green = 68, blue = 64)
private val LightGray = Color(red = 164, green = 164, blue = 163)
private val MediumGray = Color(red = 92, green = 89, blue = 84)
private val LightMediumGray = Color(red = 173, green = 173, blue = 173)
private val DarkAlmostBlack = Color(red = 50, green = 46, blue = 29)
private val DarkAlmostBlackPlus = Color(red = 32, green = 32, blue = 32)


private val EnergyOrangeDark = Color(color = 0xFFE65100)

private val FreshGreen = Color(color = 0xFF388E3C)
private val FreshGreenLight = Color(color = 0xFFC8E6C9)
private val FreshGreenDark = Color(color = 0xff225d24)

private val ErrorRed = Color(color = 0xFFD32F2F)
private val ErrorRedLight = Color(color = 0xFFFFCDD2)
private val ErrorRedDark = Color(color = 0xFF8E0000)

val darkColours = darkColorScheme(
    primary = EnergyOrangeGenerated,
    onPrimary = PureBlack,
    primaryContainer = DarkAlmostBlackPlus,
    onPrimaryContainer = EnergyOrangeGenerated,
    inversePrimary = EnergyOrangeGeneratedInverse,

    secondary = EnergyOrange,
    onSecondary = PureBlack,
    secondaryContainer = EnergyOrangeLight,
    onSecondaryContainer = EnergyOrangeDark,

    tertiary = FreshGreen,
    onTertiary = PureWithe,
    tertiaryContainer = FreshGreenLight,
    onTertiaryContainer = FreshGreenDark,

    background = PureBlack,
    onBackground = PureWithe,
    surface = DarkGray,
    onSurface = LightGray,
    surfaceVariant = MediumGray,
    onSurfaceVariant = LightMediumGray,
    surfaceTint = DarkAlmostBlack,


    inverseSurface = Color(color = 0xFFEEEEEE),
    inverseOnSurface = Color(color = 0xFF121212),


    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRedLight,
    onErrorContainer = ErrorRedDark,

    outline = Color(color = 0xFF707070),
    outlineVariant = Color(color = 0xFFD1C4E9),
    scrim = Color(color = 0x99000000),
    surfaceBright = Color(color = 0xFFFFFFFF),
    surfaceDim = Color(color = 0xFFF5F5F5),
    surfaceContainer = Color(color = 0xFFF0F0F0),
    surfaceContainerHigh = Color(color = 0xFFE0E0E0),
    surfaceContainerHighest = Color(color = 0xFFD0D0D0),
    surfaceContainerLow = Color(color = 0xFFF8F8F8),
    surfaceContainerLowest = Color(color = 0xFFFBFBFB)
)




//withe theme
private val LightGrayBackground = Color(red = 242, green = 242, blue = 242)
private val GrayBlueish = Color(red = 110, green = 113, blue = 145)
private val GrayBlueisher = Color(red = 78, green = 75, blue = 102)

private val LightGrayBackgroundSurface = Color(red = 252, green = 252, blue = 252)
private val DarkBlueBackground = Color(red = 20, green = 20, blue = 42)
private val LightYellowBackground = Color(red = 254, green = 236, blue = 186)


private val grayPurple = Color(red = 214, green = 208, blue = 253)
private val grayPurpleLight = Color(red = 239, green = 236, blue = 254)


private val softBackground = Color(red = 245, green = 245, blue = 245)
private val softOnBackground = Color(red = 143, green = 143, blue = 143)


private val softSurface = Color(red = 229, green = 229, blue = 229)
private val onsoftSurface = Color(red = 102, green = 104, blue = 106)


private val softSurfaceVariant = Color(red = 248, green = 248, blue = 252)




val lightColours = lightColorScheme(
    primary = EnergyOrangeGenerated,
    onPrimary = PureWithe,
    primaryContainer = LightGrayBackground,
    onPrimaryContainer = GrayBlueish,
    inversePrimary = GrayBlueisher,

    secondary = LightGrayBackgroundSurface,
    onSecondary = DarkBlueBackground,
    secondaryContainer = LightYellowBackground,
    onSecondaryContainer = DarkAlmostBlackPlus,

    tertiary = grayPurpleLight,
    onTertiary = DarkGray,
    tertiaryContainer = grayPurple,
    onTertiaryContainer = DarkBlueBackground,

    background = softBackground,
    onBackground = softOnBackground,
    surface = softSurface,
    onSurface = onsoftSurface,
    surfaceVariant = softSurfaceVariant,
    onSurfaceVariant = LightGray,
    surfaceTint = DarkAlmostBlack,
    inverseSurface = PureBlack,
    inverseOnSurface = LightWithe,

    error = ErrorRedLight,
    onError = ErrorRedDark,
    errorContainer = ErrorRedDark,
    onErrorContainer = ErrorRedLight,

    outline = Color(color = 0xFFBBBBBB),
    outlineVariant = Color(color = 0xFF757575),
    scrim = Color(color = 0x99000000),
    surfaceBright = Color(color = 0xFF2A2A2A),
    surfaceDim = Color(color = 0xFF181818),
    surfaceContainer = Color(color = 0xFF222222),
    surfaceContainerHigh = Color(color = 0xFF2E2E2E),
    surfaceContainerHighest = Color(color = 0xFF393939),
    surfaceContainerLow = Color(color = 0xFF171717),
    surfaceContainerLowest = Color(color = 0xFF1C1C1C)
)
