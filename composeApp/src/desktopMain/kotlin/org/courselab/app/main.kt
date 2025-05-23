package org.courselab.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        alwaysOnTop = true,
        title = "CourseLab",
    ) {
        App(
            httpClient = createHttpClient(),
        )
    }
}