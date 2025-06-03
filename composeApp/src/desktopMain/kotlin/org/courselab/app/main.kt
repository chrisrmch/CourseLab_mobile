package org.courselab.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.compose_multiplatform
import org.courselab.app.di.initKoin
import org.jetbrains.compose.resources.painterResource


fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        alwaysOnTop = true,
        title = "CourseLab",
    ) {
        App(painterResource(Res.drawable.compose_multiplatform))
    }
}