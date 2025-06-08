package org.courselab.app

import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.getSystemService
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.get
import java.util.Locale

class AndroidAppLocaleManager(
    private val context: Context,
) : AppLocaleManager {

    private val localeManager = context.getSystemService<LocaleManager>()

    override fun getLocale(): String {

//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            val locales = localeManager?.applicationLocales ?: return "en"
//            if (locales.isEmpty) "en" else
//                locales[0]?.toLanguageTag()?.split("-")?.firstOrNull() ?: "en"
//        } else {
//            AppCompatDelegate.getApplicationLocales()
//                .toLanguageTags().split("-")
//                .firstOrNull() ?: "en"
//        }

        val systemLocaleString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val systemLocales = localeManager?.systemLocales

            println("System Locales from LocaleManager (TIRAMISU+):")
            println("  Is systemLocales null? ${systemLocales == null}")

            if (systemLocales != null) {
                println("  Is systemLocales empty? ${systemLocales.isEmpty}")
            }

            if (systemLocales != null && !systemLocales.isEmpty) {
                for (i in 0 until systemLocales.size()) {
                    val locale = systemLocales[i]
                    println("\tSystem Locale ${i}: Tag=${locale?.toLanguageTag()}, Lang=${locale?.language}, Country=${locale?.country}, DisplayName=${locale?.displayName}")
                }
                systemLocales[0]?.toLanguageTag()
            } else {
                println("System Locales from Resources (TIRAMISU+ fallback):")
                println("  System Locale 0: ${context.resources.configuration.locales[0].toLanguageTag()}")
                context.resources.configuration.locales[0].toLanguageTag()
            }
        } else {
            val currentLocales = context.resources.configuration.locales

            println("System Locales from Resources (pre-TIRAMISU):")
            println("\tNumber of locales: ${currentLocales.size()}")

            for (i in 0 until currentLocales.size()) {
                val locale = currentLocales[i]
                println("  System Locale ${i}: Tag=${locale?.toLanguageTag()}, Lang=${locale?.language}, Country=${locale?.country}, DisplayName=${locale?.displayName}")
            }

            currentLocales[0].toLanguageTag()

        }?.split("-")?.firstOrNull() ?: "es"
        println("Final System Locale String: $systemLocaleString")

        val appLocaleString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // App-specific locales are only available from API 33+
            val appLocales = localeManager?.applicationLocales
            println("Application Locales from LocaleManager (TIRAMISU+):")
            println("  Is appLocales null? ${appLocales == null}")
            if (appLocales != null) {
                println("  Is appLocales empty? ${appLocales.isEmpty}")
            }
            if (appLocales != null && !appLocales.isEmpty) {
                for (i in 0 until appLocales.size()) {
                    val locale = appLocales[i]
                    println("  App Locale ${i}: Tag=${locale?.toLanguageTag()}, Lang=${locale?.language}, Country=${locale?.country}, DisplayName=${locale?.displayName}")
                }
                appLocales[0]?.toLanguageTag()?.split("-")?.firstOrNull() ?: "es"
            } else {
                println("No App Locale set via LocaleManager (or appLocales is empty/null), using system locale: $systemLocaleString")
                systemLocaleString // Fallback to system if no app locale is set
            }
        } else {
            // For older versions, app locale is determined by AppCompatDelegate.
            // AppCompatDelegate.getApplicationLocales() returns locales set by setApplicationLocales or an empty list.
            val appCompatLocales = AppCompatDelegate.getApplicationLocales()
            println("Application Locales from AppCompatDelegate (pre-TIRAMISU): ${appCompatLocales.toLanguageTags()}")
            appCompatLocales.toLanguageTags().split("-").firstOrNull() ?: systemLocaleString
        }

        return if (systemLocaleString.equals(appLocaleString, ignoreCase = true)) {
            println("System locale ($systemLocaleString) and App locale ($appLocaleString) are the same. Returning System Locale.")
            systemLocaleString
        } else {
            println("System locale ($systemLocaleString) and App locale ($appLocaleString) are different. Returning App Locale.")
            appLocaleString
        }


    }
}

private fun String?.toApLang(
): AppLang = when (this) {
    "en" -> AppLang.English
    "fr" -> AppLang.French
    else -> AppLang.Spanish
}


@Composable
actual fun rememberAppLocale(): AppLang {
    val localeManager: AppLocaleManager = koinInject()
    val code = localeManager.getLocale()

    println("DENTRO DE rememberAppLocale  RETURNS---->> $code")

    return remember(code) {
        code.toApLang()
    }
}


class AndroidUrlLauncher(private val context: Context) : UrlLauncher {
    override fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}

@Composable
actual fun rememberUrlLauncher(): UrlLauncher {
    val context = LocalContext.current
    return remember {
        AndroidUrlLauncher(context)
    }
}


actual fun getCalendarLocale(): CalendarLocaleWithLanguage {
    return CalendarLocaleLanguage(get().get())
}

class CalendarLocaleLanguage(
    private val localeManager: AppLocaleManager,
) : CalendarLocaleWithLanguage {
    override val language: String
        get() = localeManager.getLocale()

    @ExperimentalMaterial3Api
    override fun getPlatformCalendarLocale(): CalendarLocale {
        println("DENTRO DE ApplicationLanguage.android.kt getPlatformCalendarLocale  RETURNS---->> $language")
        val locale = Locale(language)
        println("Locale language: ${locale.language}")
        println("Locale country: ${locale.country}")
        println("Locale display name: ${locale.displayName}")
        println("Locale ISO3 language: ${locale.isO3Language}")
        println("Locale ISO3 country: ${locale.isO3Country}")
        return Locale(language)
    }
}

