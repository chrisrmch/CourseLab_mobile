package  org.courselab.app.ui.screens.sign_in.composables


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.courselab.app.data.UserPreferencesDataStore
import org.koin.compose.koinInject


@Composable
fun ThemeToggle(
    userPreferences: UserPreferencesDataStore = koinInject(),
    modifier: Modifier = Modifier
) {
    val themePref by userPreferences.themePreference.collectAsState(initial = "system")
    val isDark = (themePref == "dark")
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = if (isDark) "Dark Mode" else "Light Mode",
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = isDark,
            onCheckedChange = { nuevoValor ->
                scope.launch {
                    userPreferences.setThemePreference(if (nuevoValor) "dark" else "light")
                }
            }
        )
    }
}