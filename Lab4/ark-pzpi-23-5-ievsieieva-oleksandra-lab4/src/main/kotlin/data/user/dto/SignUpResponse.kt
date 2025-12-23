package data.user.dto

import kotlinx.serialization.Serializable
import ua.nure.data.user.User
import ua.nure.db.mapping.UserDao

@Serializable
data class SignUpResponse(
    val user: UserDto,
    val token: String,
)
