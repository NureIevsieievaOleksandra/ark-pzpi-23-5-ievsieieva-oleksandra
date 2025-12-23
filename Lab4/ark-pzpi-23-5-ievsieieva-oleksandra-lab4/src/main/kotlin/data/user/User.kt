package ua.nure.data.user

import kotlinx.serialization.Serializable

data class User(
    val userId: Int? = null,
    val name: String,
    val password: String,
    val salt: String,
    val role: Role? = null,
)

enum class Role {
    Admin, User, Undefined
}