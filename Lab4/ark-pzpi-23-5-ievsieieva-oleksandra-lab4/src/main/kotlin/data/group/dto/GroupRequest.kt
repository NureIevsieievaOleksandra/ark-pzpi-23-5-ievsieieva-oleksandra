package ua.nure.data.group.dto

import kotlinx.serialization.Serializable

@Serializable
data class GroupRequest(
    val groupId: Int? = null,
    val name: String? = null,
    val description: String? = null,
)