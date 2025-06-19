package org.courselab.app.ui.screens.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import org.courselab.app.ui.screens.homeNavigation.TargetMapboxMap
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold


@Composable
fun Activity(
    navController: NavHostController
){
    GradientScaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.waterfall).background(color = Color.Transparent)
    ) {


        Box(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.waterfall)){
            TargetMapboxMap(
                modifier = Modifier,
                navController = navController
            )
        }
    }
}