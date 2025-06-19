@file:OptIn(ExperimentalMaterial3Api::class)

package org.courselab.app.ui.screens.homeNavigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.home
import courselab.composeapp.generated.resources.ic_run
import courselab.composeapp.generated.resources.profile
import kotlinx.serialization.Serializable
import org.courselab.app.LocalLocationPermissionGranted
import org.courselab.app.LocalRequestLocationPermission
import org.courselab.app.ui.screens.activity.Activity
import org.courselab.app.ui.screens.home.MainActivityPage
import org.courselab.app.ui.screens.homeNavigation.composables.CourseLabBottomBar
import org.courselab.app.ui.screens.profile.UserProfileDetailsScreen
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Serializable
sealed class HomeRoute(val route: String) {
    @Serializable
    data object Home : HomeRoute("home")

    @Serializable
    data object Profile : HomeRoute("home/profile")

    @Serializable
    data object Activity : HomeRoute("home/activity")
}

@Composable
fun HomeScreenNavigationHolder() {
    val homeNav = rememberNavController()
    val tabs = listOf(
        BottomNavItem(Icons.Default.Home, stringResource(Res.string.home), "home"),
        BottomNavItem(Icons.Default.Person, stringResource(Res.string.profile), "home/profile")
    )

    val navBackStackEntry by homeNav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        HomeRoute.Home.route,
        HomeRoute.Profile.route
    )
    val requestPermission = LocalRequestLocationPermission.current
    val granted = LocalLocationPermissionGranted.current
    val shouldNavigateToRun = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(granted, shouldNavigateToRun.value) {
        if (granted && shouldNavigateToRun.value) {
            homeNav.navigate(HomeRoute.Activity.route) {
                launchSingleTop = true
            }
            shouldNavigateToRun.value = false
        }
    }

    GradientScaffold(
        bottomBar = {
            if (showBottomBar) {
                CourseLabBottomBar(navController = homeNav, items = tabs)
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                FloatingActionButton(
                    modifier = Modifier.offset(y = (53).dp),
                    shape = RoundedCornerShape(percent = 50),
                    onClick = {
                        if (!granted) {
                            shouldNavigateToRun.value = true
                            requestPermission()
                        } else {
                            homeNav.navigate(HomeRoute.Activity.route)
                        }
                    },
                    containerColor = colorScheme.tertiary,
                    contentColor = colorScheme.onTertiary
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_run),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController = homeNav,
            startDestination = HomeRoute.Home.route,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            composable(HomeRoute.Home.route) {
                MainActivityPage(homeNav)
            }
            composable(HomeRoute.Profile.route) {
                UserProfileDetailsScreen()
            }
            composable(HomeRoute.Activity.route) {
                Activity(homeNav)
            }
        }
    }
}

data class BottomNavItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)
