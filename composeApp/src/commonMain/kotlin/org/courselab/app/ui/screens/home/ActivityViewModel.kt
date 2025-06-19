package org.courselab.app.ui.screens.home

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.data.repository.ActivityRepository
import org.courselab.app.models.ActividadDTO
import org.courselab.app.viewmodel.BaseViewModel


@Immutable
data class ActivityState(
    var activities: List<ActividadDTO> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class ActivityViewModelCommon(
    private val activityRepository: ActivityRepository,
    private val userPreferences: UserPreferencesDataStore,
) : BaseViewModel() {

    private val _state = MutableStateFlow(ActivityState())
    val state: StateFlow<ActivityState> = _state

    init {
        scope.launch {
            _state.update { it.copy(isLoading = true) }

            runCatching { activityRepository.getAllUserActivities() }
                .onSuccess { list ->
                    _state.update { it.copy(activities = list, isLoading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
        }
    }

    suspend fun getAllActivities() {
        _state.update { it.copy(isLoading = true) }

        runCatching { activityRepository.getAllUserActivities() }
            .onSuccess { list ->
                _state.update { it.copy(activities = list, isLoading = false) }
            }
            .onFailure { e ->
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
    }

    fun refreshActivities() {
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            runCatching { activityRepository.getAllUserActivities() }
                .onSuccess { list ->
                    _state.update { it.copy(activities = list, isLoading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
        }
    }



}