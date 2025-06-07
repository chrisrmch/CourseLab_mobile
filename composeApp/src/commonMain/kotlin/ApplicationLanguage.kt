package org.courselab.app

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


interface AppLocaleManager {
    fun getLocale(): String
}

@Composable
expect fun rememberAppLocale(): AppLang

//
//@Composable
//fun MyApp() {
//    Text(stringResource(Res.strings.app_name))
//    Text(stringResource(Res.strings.greeting))
//    Text(stringResource(Res.strings.welcome_message, "User"))
//}