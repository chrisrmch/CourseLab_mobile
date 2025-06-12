package org.courselab.app.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold

@Composable
fun RunningActivity(
    modifier: Modifier = Modifier,
) {
    GradientScaffold { it ->
        TargetMapboxMap(modifier.fillMaxSize())
    }
}

@Composable
expect fun TargetMapboxMap(modifier: Modifier)