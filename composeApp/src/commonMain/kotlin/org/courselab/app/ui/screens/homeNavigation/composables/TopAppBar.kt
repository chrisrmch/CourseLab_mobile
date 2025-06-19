@file:OptIn(ExperimentalMaterial3Api::class)

package org.courselab.app.ui.screens.homeNavigation.composables

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import courselab.composeapp.generated.resources.Res
import courselab.composeapp.generated.resources.logo_dark
import courselab.composeapp.generated.resources.logo_light
import courselab.composeapp.generated.resources.menu
import org.courselab.app.data.UserPreferencesDataStore
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CourseLabTopAppbar(userPrefs: UserPreferencesDataStore) {
    TopAppBar(
        modifier = Modifier.shadow(elevation = 0.dp)
            .clip(shape = RoundedCornerShape(
                bottomStart = 20.dp,
                bottomEnd = 20.dp
            )
            ),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.surfaceContainer,
            titleContentColor = colorScheme.primary,
        ),
        title = {
            if (userPrefs.themePreference.equals("dark")) {
                HomeHeader(
                    logo = painterResource(Res.drawable.logo_light),
                    modifier = Modifier.offset(x = (-20).dp)
                )
            } else HomeHeader(
                logo = painterResource(Res.drawable.logo_dark),
                Modifier.offset(x = (-20).dp)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {

                },
                modifier = Modifier,
                enabled = true,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = colorScheme.tertiary,
                    contentColor = colorScheme.onTertiary,
                ),
                content = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(Res.string.menu)
                    )
                }
            )
        },
    )
}