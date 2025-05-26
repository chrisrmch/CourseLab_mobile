package org.courselab.app.ui.screens.sign_in

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.courselab.app.data.AuthRepository
import org.courselab.app.data.LoginRequest
import org.courselab.app.viewmodel.BaseViewModel

//DTO
data class LoginFormState(
    val email: String = "", val password: String = "", val isValid: Boolean = false,
)

data class ForgotPassword(val email: String)


//CONTROL DEL ESTADO
class LogInViewModel(private val repository: AuthRepository) : BaseViewModel() {

    private val _loginState = MutableStateFlow(value = LoginFormState())
    val loginState: StateFlow<LoginFormState> = _loginState

    private val _snackbarMsg = MutableSharedFlow<String>()
    val snackbarMsg: SharedFlow<String> = _snackbarMsg

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onLoginInputChanged(email: String, password: String) {
        _loginState.value = LoginFormState(email, password, email.isNotBlank() && password.isNotBlank())
    }

    fun onLogInEvent(logInRequest: LoginRequest, onResult: (Boolean) -> Unit) {
        scope.launch {
            _isLoading.value = true
            try {
                var response = repository.logIn(logInRequest.email, logInRequest.password)
                println(response)
                if (response.success) onResult(true)
                else {
                    _snackbarMsg.emit(response.message ?: "Error en login")
                    onResult(false)
                }
            } catch (e: Exception) {
                print(e)
                _snackbarMsg.emit("Error de red: ${e.message}")
                onResult(false)
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