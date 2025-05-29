package org.courselab.app.ui.screens.sign_up


import io.ktor.util.toLowerCasePreservingASCIIRules
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.courselab.app.data.AuthRepository
import org.courselab.app.data.LogInResponse
import org.courselab.app.viewmodel.BaseViewModel


data class SignUpFormState(
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val password: String = "",
    val fechaNacimiento: String = "",
    val genero: String = "",
    val isValid: Boolean = false,
)


data class SignUp(val form: SignUpFormState)

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
        println("se ha llegado aqui + $value")
//         _loginState.value =
//            LoginFormState(email, password, email.isNotBlank() && password.isNotBlank())
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

    fun onSignUpFormSubmitted(event: SignUp, onResult: (Boolean) -> Unit) {
        scope.launch {
            _isLoading.value = true
            try {
                val req = LogInResponse(
                    event.form.nombre,
                    event.form.apellidos,
                    event.form.email,
                    event.form.password,
                    event.form.fechaNacimiento,
                    event.form.genero
                )

                val response = authRepository.signUp(req)
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
