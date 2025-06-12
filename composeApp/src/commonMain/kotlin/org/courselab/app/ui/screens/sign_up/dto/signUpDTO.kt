package org.courselab.app.org.courselab.app.ui.screens.sign_up.dto
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDTO(
    val email: String,
    val password: String,
    val role: String = "ROLE_USER",
)

/**
firstname": "$firstname",
"lastname": "$lastname",
"email": "$email",
"password": "$password",
"role": "ROLE_USER"
 */