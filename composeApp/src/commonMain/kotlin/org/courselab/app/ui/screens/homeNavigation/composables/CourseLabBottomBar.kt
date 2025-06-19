package org.courselab.app.ui.screens.homeNavigation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.courselab.app.screenDetails
import org.courselab.app.ui.screens.homeNavigation.BottomNavItem

@Composable
fun CourseLabBottomBar(
    navController: NavHostController,
    items: List<BottomNavItem>
) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    Box{
        NavigationBar(
            windowInsets = WindowInsets.waterfall,
            tonalElevation = 4.dp,
            modifier = Modifier.windowInsetsPadding(WindowInsets.waterfall).clip(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            ).clipToBounds().height(80.dp).shadow(elevation = 4.dp),
            containerColor = colorScheme.surfaceContainer,
            contentColor = colorScheme.primary,
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                val tint = if (selected)
                    colorScheme.primary
                else
                    colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

                Row(
                    modifier = Modifier.width((screenDetails().widthDp / items.size).dp)
                        .fillMaxHeight()
                ) {
                    NavigationBarItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = tint,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = tint,
                                maxLines = 1
                            )
                        },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
