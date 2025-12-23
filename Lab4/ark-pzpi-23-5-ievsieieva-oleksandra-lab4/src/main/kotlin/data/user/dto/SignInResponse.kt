package data.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    val token: String,
    val userId: Int?,
    val userName: String?,
    val role: Int? = null,
)
