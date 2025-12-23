package ua.nure.data.lamp

import kotlinx.serialization.Serializable

@Serializable
data class Lamp(
    val lampId: Int? = null,
    val name: String? = null,
    val groupId: Int? = null,
    val description: String? = null,
    val r: Int? = null,
    val g: Int? = null,
    val b: Int? = null,
    val brightness: Int? = null,
    val active: Boolean = false,
)
