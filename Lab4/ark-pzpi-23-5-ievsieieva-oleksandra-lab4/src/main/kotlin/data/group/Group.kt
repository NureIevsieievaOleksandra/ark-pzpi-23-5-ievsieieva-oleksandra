package ua.nure.data.group

import kotlinx.serialization.Serializable
import ua.nure.data.lamp.Lamp

@Serializable
data class Group(
    val groupId: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val lamps: List<Lamp> = emptyList(),
)
