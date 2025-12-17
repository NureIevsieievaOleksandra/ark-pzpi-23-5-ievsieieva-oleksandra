package data.user.dto

import kotlinx.serialization.Serializable
import ua.nure.data.user.User

@Serializable
data class UpdateUserRequest(
    val userId: Int,
    val role: Int,
)