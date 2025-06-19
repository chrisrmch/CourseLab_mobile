package org.courselab.app.ui.screens.onboarding.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.courselab.app.data.UserPreferencesDataStore
import org.koin.compose.koinInject

@Composable
fun RequestDetailsCard(
    modifier: Modifier = Modifier,
    body: @Composable () -> Unit,
) {
    val datastore = koinInject<UserPreferencesDataStore>()
    val themePreferences = datastore.themePreference.collectAsState(initial = "System")
    println("theme preferemce: ${themePreferences.value}")
    Surface(
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(25.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(25.dp),
                ambientColor = MaterialTheme.colorScheme.onSecondaryContainer,
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            ).offset(y = (-4).dp, x = (-4).dp)
            .then(modifier).border( 1.dp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), shape = RoundedCornerShape(25.dp))
    ) {
        Column {
            body()
        }
    }
}
