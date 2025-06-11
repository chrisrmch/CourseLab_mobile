package org.courselab.app.org.courselab.app.ui.screens.log_in.dto

data class ForgotPasswordDTO(
    val email: String,
    val isValid: Boolean = Validator.validateEmail(email),
)