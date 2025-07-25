package org.courselab.app

import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

class IosAppLocaleManager : AppLocaleManager {
    override fun getLocale(): String {
        val nsLocale = NSLocale.currentLocale.languageCode
        return nsLocale
    }
}

@Composable
actual fun rememberAppLocale(): AppLang {
    val nsLocale = IosAppLocaleManager().getLocale()
    return remember(nsLocale) {
        when (nsLocale) {
            "en" -> AppLang.English
            "fr" -> AppLang.French
            else -> AppLang.Spanish
        }
    }
}

@Composable
actual fun rememberUrlLauncher(): UrlLauncher {
    TODO("Not yet implemented")
}

actual fun getCalendarLocale(): CalendarLocaleWithLanguage {
    TODO("Not yet implemented")
}

@Composable
@ReadOnlyComposable
@ExperimentalMaterial3Api
internal actual fun defaultLocale(): CalendarLocale {
    TODO("Not yet implemented")
}