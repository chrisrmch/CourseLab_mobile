package org.courselab.app.ui.screens.onboarding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy


@Composable
actual fun getDialogProperties(title: String): DialogProperties {
    return DialogProperties(
        usePlatformDefaultWidth = false,
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.SecureOn,
        windowTitle = title
    )
}