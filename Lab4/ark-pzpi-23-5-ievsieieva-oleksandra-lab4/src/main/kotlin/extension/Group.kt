package ua.nure.extension

import ua.nure.data.user.Role

fun String?.toRole() = when(this) {
    "User" -> Role.User
    "Admin" -> Role.Admin
    else -> Role.Undefined
}