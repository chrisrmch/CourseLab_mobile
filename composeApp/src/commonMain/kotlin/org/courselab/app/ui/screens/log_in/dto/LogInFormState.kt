package org.courselab.app.org.courselab.app.ui.screens.log_in.dto

data class LoginFormState(
    val email: String = "", val password: String = "", val isValid: Boolean = Validator.validateEmail(email) && Validator.validatePassword(password),
)
