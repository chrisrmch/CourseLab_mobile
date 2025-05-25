package org.courselab.app.models

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val profileID: Int,
    val user: User,
    val profileImage: String,
    val description: String,
    val siteUrl: String,
    val localization: String,
    val interests: List<String>,
)