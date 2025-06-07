package org.courselab.app.ui.screens.sign_in

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.courselab.app.data.ApiResponse
import org.courselab.app.data.AuthRepository
import org.courselab.app.data.LogInResponse
import org.courselab.app.data.LoginRequestDTO
import org.courselab.app.viewmodel.BaseViewModel

//DTO
data class LoginFormState(
    val email: String = "", val password: String = "", val isValid: Boolean = false,
)

data class ForgotPassword(val email: String)

class LogInViewModel(
    private val repository: AuthRepository,
) : BaseViewModel() {
    private val _loginState = MutableStateFlow(value = LoginFormState())
    val loginState: StateFlow<LoginFormState> = _loginState

    private val _snackbarMsg = MutableSharedFlow<String>()
    val snackbarMsg: SharedFlow<String> = _snackbarMsg

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onLoginInputChanged(email: String, password: String) {
        _loginState.value =
            LoginFormState(email, password, email.isNotBlank() && password.isNotBlank())
    }

    fun onLogInEvent(logInRequest: LoginRequestDTO, onResult: (success : Boolean, firstLogin : Boolean) -> Unit) {
        scope.launch {
            _isLoading.value = true
            try {
                val response: ApiResponse<LogInResponse> =
                    repository.logIn(logInRequest.email, logInRequest.password)
                println(response)
                if (response.success && response.data != null && !response.data.firstLogin) {
                    onResult(true, false)
                    _snackbarMsg.emit(response.message ?: "Error en login")
                }
                else if (response.success && response.data != null && response.data.firstLogin) {
                    onResult(true, true)
                    _snackbarMsg.emit(response.message ?: "Bienvenido a CourseLab, por favor completa tu perfil")
                }
                else {
                    onResult(false, false)
                    _snackbarMsg.emit(response.message ?: "Error en login")
                }
            } catch (e: Exception) {
                print(e)
                onResult(false, false)
                _snackbarMsg.emit("Error de red: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onForgotPassword(password: ForgotPassword, onResult: (Boolean) -> Unit) {
        scope.launch {
            try {
                _isLoading.value = true
                _snackbarMsg.emit("Enlace de recuperación enviado")
                onResult(true)
            } catch (e: Exception) {
                println(e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }
}