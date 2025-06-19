package org.courselab.app.ui.screens.homeNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
expect fun TargetMapboxMap(modifier: Modifier, navController: NavHostController)