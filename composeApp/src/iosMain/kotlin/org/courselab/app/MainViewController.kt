package org.courselab.app

import androidx.compose.ui.window.ComposeUIViewController
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

fun MainViewController() = ComposeUIViewController { App(painterResource(Res.drawable.compose_multiplatform)) }