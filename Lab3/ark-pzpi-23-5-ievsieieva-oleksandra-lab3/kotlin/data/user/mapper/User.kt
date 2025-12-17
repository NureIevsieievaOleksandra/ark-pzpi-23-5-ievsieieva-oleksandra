package data.user.mapper

import data.user.dto.UserDto
import ua.nure.data.user.User

fun User.toDto() = UserDto(
    userId = userId,
    name = name,
    role = role.asDtoValue()
)