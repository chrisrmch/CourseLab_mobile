package org.courselab.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual fun screenDetails(): ScreenDetails {
    TODO("Not yet implemented")
}

@Composable
actual fun CameraView(modifier: Modifier) {
    TODO("Not yet implemented")
}