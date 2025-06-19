@file:OptIn(ExperimentalMaterial3Api::class)

package org.courselab.app.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.courselab.app.data.repository.ActivityRepository
import org.courselab.app.models.ActividadDTO
import org.courselab.app.ui.screens.homeNavigation.composables.CourseLabTopAppbar
import org.courselab.app.ui.screens.sign_in.composables.GradientScaffold
import org.koin.compose.koinInject

@Composable
fun MainActivityPage(nav: NavHostController) {
    val viewModel: ActivityViewModelCommon = koinInject()
    val activityRepository = koinInject<ActivityRepository>()
    val ui by viewModel.state.collectAsState()
    val activityList : List<ActividadDTO>  = ui.activities.map { it.copy() }.toList()

    LaunchedEffect(Unit){
        viewModel.scope.launch {
            activityRepository.getAllUserActivities()
        }
    }


    GradientScaffold(
        topBar = { CourseLabTopAppbar(koinInject()) }) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn {
                items(activityList) { activity: ActividadDTO ->
                    Spacer(modifier = Modifier.height(10.dp))
                    if (activity == ui.activities.last()) {
                        ActivityCard(modifier = Modifier, actividad = activity, onClick = {

                        })
                        Spacer(modifier = Modifier.height(10.dp))
                    } else {
                        ActivityCard(modifier = Modifier, actividad = activity, onClick = {

                        })
                    }
                }

            }
        }
    }
}


@Composable
expect fun ActivityCard(
    modifier: Modifier, onClick: (ActividadDTO) -> Unit = {}, actividad: ActividadDTO
)