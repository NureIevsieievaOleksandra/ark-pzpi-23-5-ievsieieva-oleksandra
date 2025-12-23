package data.user.dto

import kotlinx.serialization.Serializable
import ua.nure.data.user.Role

@Serializable
data class UserDto(
    val userId: Int? = null,
    val name: String,
    val role: Int? = null,
)
