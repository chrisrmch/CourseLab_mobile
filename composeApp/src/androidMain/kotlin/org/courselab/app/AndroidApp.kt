package org.courselab.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun AndroidApp() {
    val logoPainter = painterResource(id = R.drawable.courselab_inside_app)
    App(logo = logoPainter)
}