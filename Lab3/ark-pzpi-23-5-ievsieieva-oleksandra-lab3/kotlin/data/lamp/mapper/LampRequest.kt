package ua.nure.data.lamp.mapper

import ua.nure.data.lamp.Lamp
import ua.nure.data.lamp.dto.LampRequest

fun LampRequest.toModel() =
    Lamp(
        lampId = lampId,
        name = name,
        description = description,
        groupId = groupId,
        r = r,
        g = g,
        b = b,
        brightness = brightness,
        active = active,
    )