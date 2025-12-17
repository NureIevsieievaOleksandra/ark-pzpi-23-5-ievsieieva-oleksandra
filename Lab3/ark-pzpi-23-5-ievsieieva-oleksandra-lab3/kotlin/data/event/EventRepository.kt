package ua.nure.data.event

import ua.nure.data.event.dto.ColorContest

interface EventRepository {
    suspend fun createEvent(event: Event): Event
    suspend fun getEvents(): List<Event>
    suspend fun getByColors(): List<ColorContest>
    suspend fun getMathExpectationR(): Pair<Double, Double>
    suspend fun getMathExpectationG(): Pair<Double, Double>
    suspend fun getMathExpectationB(): Pair<Double, Double>
}