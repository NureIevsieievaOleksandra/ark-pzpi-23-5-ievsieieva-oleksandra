package data.user.mapper

import ua.nure.data.user.Role

fun Role?.asDtoValue() =
    when (this) {
        Role.Admin -> 0
        Role.User -> 1
        Role.Undefined -> 2
        null -> null
    }

fun Int?.toRole() = when(this) {
    0 -> Role.Admin
    1 -> Role.User
    2 -> Role.Undefined
    else -> throw IllegalArgumentException("Role not supported")
}