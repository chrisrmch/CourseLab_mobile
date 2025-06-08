package org.courselab.app

import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.en
import courselab.composeapp.generated.resources.es
import courselab.composeapp.generated.resources.fr
import org.jetbrains.compose.resources.StringResource

enum class AppLang(
    val code: String,
    val stringRes: StringResource,
) {
    Spanish("es", Res.string.es),
    English("en", Res.string.en),
    French("fr", Res.string.fr)
}

@OptIn(ExperimentalMaterial3Api::class)
interface CalendarLocaleWithLanguage {
    val language: String
    fun getPlatformCalendarLocale(): CalendarLocale
}

interface AppLocaleManager {
    fun getLocale(): String
}

@Composable
expect fun rememberAppLocale(): AppLang


interface UrlLauncher {
    fun openAppSettings()
}


@Composable
expect fun rememberUrlLauncher(): UrlLauncher

expect fun getCalendarLocale() : CalendarLocaleWithLanguage
