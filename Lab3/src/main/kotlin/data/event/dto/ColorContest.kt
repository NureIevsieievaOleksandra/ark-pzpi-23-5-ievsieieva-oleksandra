package ua.nure.data.event.dto

import kotlinx.serialization.Serializable

@Serializable
data class ColorContest(
    val r: Int? = null,
    val g: Int? = null,
    val b: Int? = null,
    val count: Long,
)
