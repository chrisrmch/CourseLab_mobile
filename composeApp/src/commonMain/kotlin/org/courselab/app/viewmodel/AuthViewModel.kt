package org.courselab.app.viewmodel


import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.courselab.app.data.AuthRepository
import org.courselab.app.data.SignUpRequest

data class LoginFormState(
    val email: String = "", val password: String = "", val isValid: Boolean = false
)

data class SignUpFormState(
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val password: String = "",
    val fechaNacimiento: String = "",
    val genero: String = "",
    val isValid: Boolean = false
)

sealed class AuthEvent {
    data class Login(val email: String, val password: String) : AuthEvent();
    data class SignUp(val form: SignUpFormState) : AuthEvent();
    data class ForgotPassword(val email: String) : AuthEvent()
}

class AuthViewModel(
    httpClient: HttpClient, baseUrl: String
) {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val repository = AuthRepository(httpClient, baseUrl)

    private val _loginState = MutableStateFlow(LoginFormState())
    val loginState: StateFlow<LoginFormState> = _loginState

    private val _signUpState = MutableStateFlow(SignUpFormState())
    val signUpState: StateFlow<SignUpFormState> = _signUpState

    private val _snackbarMsg = MutableSharedFlow<String>()
    val snackbarMsg: SharedFlow<String> = _snackbarMsg

    fun onLoginInputChanged(email: String, password: String) {
        _loginState.value =
            LoginFormState(email, password, email.isNotBlank() && password.isNotBlank())
    }

    fun onSignUpInputChanged(field: String, value: String) {
        val current = _signUpState.value
        val updated = when (field) {
            "nombre" -> current.copy(nombre = value)
            "apellidos" -> current.copy(apellidos = value)
            "e-mail" -> current.copy(email = value)
            "contraseña" -> current.copy(password = value)
            "fecha de nacimiento" -> current.copy(fechaNacimiento = value)
            "género" -> current.copy(genero = value)
            else -> current
        }
        _signUpState.value = updated.copy(
            isValid = updated.nombre.isNotBlank()
                    && updated.apellidos.isNotBlank()
                    && updated.email.isNotBlank()
                    && updated.password.isNotBlank()
        )
    }

    fun handleEvent(event: AuthEvent, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            _isLoading.value = true
            try {
                when (event) {
                    is AuthEvent.Login -> {
                        val resp = repository.login(event.email, event.password)
                        print(resp)
                        if (resp.success) onResult(true)
                        else _snackbarMsg.emit(resp.message ?: "Error en login")
                    }

                    is AuthEvent.SignUp -> {
                        val req = SignUpRequest(
                            event.form.nombre,
                            event.form.apellidos,
                            event.form.email,
                            event.form.password,
                            event.form.fechaNacimiento,
                            event.form.genero
                        )
                        val resp = repository.signUp(req)
                        if (resp.success) onResult(true)
                        else _snackbarMsg.emit(resp.message ?: "Error en registro")
                    }

                    is AuthEvent.ForgotPassword -> {
                        _snackbarMsg.emit("Enlace de recuperación enviado")
                        onResult(true)
                    }
                }
            } catch (e: Exception) {
                print(e)
                _snackbarMsg.emit("Error de red: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
