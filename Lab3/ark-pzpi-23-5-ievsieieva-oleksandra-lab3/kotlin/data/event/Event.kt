package ua.nure.data.event

import java.time.LocalDateTime

data class Event(
    val eventId: Int? = null,
    val lampId: Int? = null,
    val groupId: Int? = null,
    val r: Int? = null,
    val g: Int? = null,
    val b: Int? = null,
    val brightness: Int? = null,
    val active: Boolean = false,
    val date: LocalDateTime = LocalDateTime.now(),
)
