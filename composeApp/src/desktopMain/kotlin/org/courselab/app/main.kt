package org.courselab.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.courselab.app.di.initKoin


fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        alwaysOnTop = true,
        title = "CourseLab",
    ) {
        App()
    }
}