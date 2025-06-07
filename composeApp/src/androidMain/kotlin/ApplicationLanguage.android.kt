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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.getSystemService
import org.koin.compose.koinInject
import java.util.Locale

class AndroidAppLocaleManager(
    private val context: Context,
) : AppLocaleManager {

    private val localeManager = context.getSystemService<LocaleManager>()

    override fun getLocale(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val locales = localeManager?.applicationLocales ?: return "en"
            if (locales.isEmpty) "en" else
                locales[0]?.toLanguageTag()?.split("-")?.firstOrNull() ?: "en"
        } else {
            AppCompatDelegate.getApplicationLocales()
                .toLanguageTags().split("-")
                .firstOrNull() ?: "en"
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


actual fun getCalendarLocale(): CalendarLocale {
    return Locale.getDefault() as CalendarLocale
}


@Composable
@ReadOnlyComposable
@ExperimentalMaterial3Api
internal actual fun defaultLocale(): CalendarLocale {
    val config = LocalConfiguration.current
    val locale =
        config.locales[0]
    return locale as CalendarLocale
}