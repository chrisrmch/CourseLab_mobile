package org.courselab.app.ui.screens.onboarding.viewModel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.data.repository.UserRepository
import org.courselab.app.models.PerfilUsuarioResponseDTO
import org.courselab.app.models.TrainingActivity
import org.courselab.app.viewmodel.BaseViewModel


data class UserUiState(
    val profile: PerfilUsuarioResponseDTO? = null,
    val activities: List<TrainingActivity> = emptyList(),
    val name: String = "",
    val email: String = "",
    val dateOfBirth: LocalDate? = null,
    val sex: Sex? = null,
    private val role: String = "ROLE_USER"
)


enum class Sex {
HOMBRE, MUJER
}

class UserViewModel(
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val userId : Flow<Int?> = userPreferencesDataStore.userId

    private val _uiState = MutableStateFlow(UserUiState())
    val userState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun updateUserName(name: String) {
        _uiState.update { currentState -> currentState.copy(name = name) }
        println("SE HA ACTUALIZADO EL NOMBRE")
    }

    fun updateUserEmail(email: String) {
        _uiState.update { currentState -> currentState.copy(email = email) }
    }

    fun updateUserDateOfBirth(dateOfBirth: LocalDate) {
        _uiState.update { currentState -> currentState.copy(dateOfBirth = dateOfBirth) }
        println("SE HA ACTUALIZADO LA FECHA DE NACIMIENTO")
    }

    fun updateUserSex(sex: Sex) {
        _uiState.update { currentState -> currentState.copy(sex = sex) }
        println("SE HA ACTUALIZADO EL SEXO")
    }
}
