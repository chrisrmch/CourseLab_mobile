@file:OptIn(ExperimentalMaterial3Api::class)

package org.courselab.app.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import org.courselab.app.LocalNavController
import org.courselab.app.screenDetails
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.courselab.app.ui.screens.sign_in.composables.ThemeToggle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen() {
    val size = screenDetails()
    val scope = rememberCoroutineScope()

    GradientScaffold(
        bottomBar = {
            BottomNavigationBar()
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "COURSELAB",
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        letterSpacing = TextUnit(size.widthDp * 0.025f, TextUnitType.Sp),
                        modifier = Modifier.fillMaxWidth().padding(end = 45.dp)
                    )
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    navigationIconContentColor = colorScheme.onPrimaryContainer,
                    titleContentColor = colorScheme.onPrimary,
                    actionIconContentColor = colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { /* TODO: abrir menú si quieres */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(15.dp)
        ) {
            Text("Home Screen Content")
            Spacer(modifier = Modifier.height(10.dp))
            ThemeToggle()

            val screenHeight = size.heightDp
            val screenWidth = size.widthDp
            Text("Screen Height: $screenHeight")
            Text("Screen Width: $screenWidth")
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}


data class BottomNavItem(
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavigationBar() {
    val navigator = LocalNavController.current
    val items = listOf(
        BottomNavItem(icon = Icons.Default.Home, label = "Home"),
        BottomNavItem(icon = Icons.Default.Person, label = "Profile"),
        BottomNavItem(icon = Icons.Default.Settings, label = "Settings")
    )

    var selectedIndex by remember { mutableStateOf(0) }

    NavigationBar(
        containerColor = colorScheme.primaryContainer,
        contentColor = colorScheme.onPrimaryContainer
    ) {
        items.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = (index == selectedIndex),
                onClick = {
                    selectedIndex = index

                    if (navItem.label == "Settings" && navigator != null) {
                        navigator.navigateUp()
                    }

                },
                icon = {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = navItem.icon,
                        contentDescription = navItem.label
                    )
                },
                label = {
                    Text(text = navItem.label)
                },
            )
        }
    }
}