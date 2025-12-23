package data.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val username: String,
    val password: String
)
