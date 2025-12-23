package ua.nure.data.event.dto

import kotlinx.serialization.Serializable
import ua.nure.data.iotsocket.IotStatistics
import ua.nure.data.iotsocket.IotStatisticsReview

@Serializable
data class AnalyticsDto(
    val colorContest: List<ColorContest>,
    val mathExpectationR: Double,
    val mathExpectationG: Double,
    val mathExpectationB: Double,
    val varianceR: Double,
    val varianceG: Double,
    val varianceB: Double,
    val iotStats: List<IotStatisticsReview>

)