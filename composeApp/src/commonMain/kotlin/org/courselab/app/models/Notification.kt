package org.courselab.app.models

import kotlinx.serialization.Serializable

// I have never made an app in kotlin before but as far as I know in dev. my models were always made like this... but best practice???
// TODO: Review best practices for Kotlin data classes, especially regarding nullable types and relationships (e.g., User). Consider if `usuario` should be non-nullable or if `userId` is sufficient. Also, evaluate if `fechaProgramada` should be `LocalDateTime` for better type safety and date manipulation.

@Serializable
data class Notification(
    val notificationID: Long,
    val user: User?, // Consider if User object is needed here or just userId
    val type: String?,
    val message: String?,
    val programmedDate: String?,
    val seen: Boolean? = false
)
