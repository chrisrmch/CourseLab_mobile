package org.courselab.app.ui.screens.sign_in.dto

import org.courselab.app.ui.screens.onboarding.dto.Validator

data class LogInFormStateDTO(
val email: String = "", val password: String = "", val isValid: Boolean = Validator.validateEmail(email) && Validator.validatePassword(password),
)
