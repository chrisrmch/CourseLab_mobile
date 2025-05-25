package org.courselab.app.ui.screens.sign_in

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.courselab.app.data.AuthRepository
import org.courselab.app.viewmodel.CommonBaseViewModel
import org.courselab.app.viewmodel.ForgotPassword
import org.courselab.app.viewmodel.LogIn

//DTO
data class LoginFormState(
    val email: String = "", val password: String = "", val isValid: Boolean = false,
)

//CONTROL DEL ESTADO
class LogInViewModel(private val repository: AuthRepository) : CommonBaseViewModel() {

    private val _loginState = MutableStateFlow(LoginFormState())
    val loginState: StateFlow<LoginFormState> = _loginState

    private val _snackbarMsg = MutableSharedFlow<String>()
    val snackbarMsg: SharedFlow<String> = _snackbarMsg

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onLoginInputChanged(email: String, password: String) {
        _loginState.value =
            LoginFormState(email, password, email.isNotBlank() && password.isNotBlank())
    }

    fun onLogInEvent(event: LogIn, onResult: (Boolean) -> Unit) {
        scope.launch {
            _isLoading.value = true
            try {
                var response = repository.logIn(event.email, event.password)
                println(response)
                if (response.success) onResult(true)
                else _snackbarMsg.emit(response.message ?: "Error en login")
            } catch (e: Exception) {
                print(e)
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