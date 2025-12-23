package ua.nure.data

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val message: String,
)
