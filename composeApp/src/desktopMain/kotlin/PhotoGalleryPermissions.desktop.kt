package org.courselab.app

import androidx.compose.runtime.Composable

// PermissionsManager.kt
actual class PermissionsManager actual constructor(callback: PermissionCallback) :
    PermissionHandler

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    TODO("Not yet implemented")
}