package ua.nure.data.lamp.dto

import kotlinx.serialization.Serializable

@Serializable
data class LampCommandDto (
    val r: Int,
    val g: Int,
    val b: Int,
    val brightness: Int,
)