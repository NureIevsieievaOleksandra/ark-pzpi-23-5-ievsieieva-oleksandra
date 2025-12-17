package ua.nure.data.group.mapper

import ua.nure.data.group.Group
import ua.nure.data.group.dto.GroupRequest

fun GroupRequest.toModel()= Group(
    groupId = groupId,
    name= name,
    description= description,
)