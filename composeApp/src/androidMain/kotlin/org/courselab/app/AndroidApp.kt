package org.courselab.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.courselab.app.R.drawable.courselab

@Composable
fun AndroidApp() {
    val logoPainter = painterResource(id = courselab)
    App(logo = logoPainter)
}