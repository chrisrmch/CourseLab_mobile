package org.courselab.app.models
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userID: Int,
    val profile: PerfilUsuarioResponseDTO,
    val activities: List<TrainingActivity>,
    val name: String,
    val surname : String,
    val email: String,
    val password: String,
    val dateOfBirth : String,
    val sex : String,
    private val role: String = "ROLE_USER",
)