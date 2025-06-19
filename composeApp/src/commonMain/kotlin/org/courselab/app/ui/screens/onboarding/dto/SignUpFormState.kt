package org.courselab.app.ui.screens.onboarding.dto

data class SignUpFormState(
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val password: String = "",
    val fechaNacimiento: String = "",
    val genero: String = "",
    val isValid: Boolean = Validator.validateEmail(email) && Validator.validatePassword(password),
)
