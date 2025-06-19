package org.courselab.app.ui.screens.sign_up


import io.ktor.util.toLowerCasePreservingASCIIRules
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.courselab.app.data.repository.AuthRepository
import org.courselab.app.ui.screens.onboarding.dto.SignUpFormState
import org.courselab.app.ui.screens.sign_up.dto.SignUpRequestDTO
import org.courselab.app.viewmodel.BaseViewModel



class SignUpViewModel(
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _signUpState = MutableStateFlow(SignUpFormState())
    val signUpState: StateFlow<SignUpFormState> = _signUpState

    private val _snackbarMsg = MutableSharedFlow<String>()
    val snackbarMsg: SharedFlow<String> = _snackbarMsg

    fun onSignUpInputChanged(field: String, value: String) {
        val current = _signUpState.value
        val updated = when (field.trim()
            .toLowerCasePreservingASCIIRules()) {
            "e-mail" -> current.copy(email = value)
            "password" -> current.copy(password = value)
            else -> current
        }
        _signUpState.value = updated.copy(
            email = updated.email,
            password = updated.password,
            isValid = updated.email.isNotBlank()
                    && updated.password.isNotBlank()
        )
    }

    fun onSignUpFormSubmitted(event: SignUpRequestDTO, onResult: (Boolean) -> Unit) {
        scope.launch {
            _isLoading.value = true
            try {
                val signUpRequest = SignUpRequestDTO(
                    email = event.email,
                    password = event.password,
                    role = "ROLE_USER"
                )
                val response = authRepository.signUp(signUpRequest)
                if (response.success) onResult(true)
                else _snackbarMsg.emit(response.message ?: "Error en registro")

            } catch (e: Exception) {
                println(e.message)
                _snackbarMsg.emit("Error de red: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
