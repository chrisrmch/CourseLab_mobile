package org.courselab.app.ui.screens.sign_in

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.courselab.app.data.repository.ApiResponse
import org.courselab.app.data.repository.AuthRepository
import org.courselab.app.data.repository.LogInResponse
import org.courselab.app.data.repository.LoginRequestDTO
import org.courselab.app.ui.screens.onboarding.dto.Validator
import org.courselab.app.ui.screens.sign_in.dto.ForgotPasswordDTO
import org.courselab.app.ui.screens.sign_in.dto.LogInFormStateDTO
import org.courselab.app.viewmodel.BaseViewModel

class LogInViewModel(
    private val repository: AuthRepository,
) : BaseViewModel() {
    private val _loginState = MutableStateFlow(value = LogInFormStateDTO())
    val loginState: StateFlow<LogInFormStateDTO> = _loginState

    private val _snackbarMsg = MutableSharedFlow<String>()
    val snackbarMsg: SharedFlow<String> = _snackbarMsg

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onLoginInputChanged(email: String, password: String) {
        _loginState.value = LogInFormStateDTO(email, password, Validator.validateEmail(email) && Validator.validatePassword(password))
    }

    fun onLogInEvent(logInRequest: LoginRequestDTO, onResult: (success : Boolean, firstLogin : Boolean) -> Unit) {
        scope.launch {
            _isLoading.value = true
            try {
                val response: ApiResponse<LogInResponse> = repository.logIn(logInRequest.email, logInRequest.password)
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

    fun onForgotPassword(password: ForgotPasswordDTO, onResult: ((Boolean) -> Unit), recoveryLinkSent: String) {
        scope.launch {
            try {
                _isLoading.value = true
                _snackbarMsg.emit(recoveryLinkSent)
                onResult(true)
            } catch (e: Exception) {
                println(e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }
}