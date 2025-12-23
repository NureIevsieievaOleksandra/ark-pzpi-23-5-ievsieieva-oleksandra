package ua.nure.data.iotsocket

import kotlinx.serialization.Serializable
import ua.nure.data.iotsocket.dto.EnergyDto
import java.time.LocalDateTime

data class IotStatistics(
    val id: Int,
    val lampId: Int,
    val energyToday: Double,
    val lastEnergyUpdate: Long,
    val temperature: Double,
    val uptime: Long,
    val timestamp: LocalDateTime
)

@Serializable
data class IotStatisticsReview (
    val lampId: Int,
    val powerConsumption: Double,
    val iotTemperatureChart: List<IotTemperatureChart>,
    val uptime: Long,
)

@Serializable
data class IotTemperatureChart (
    val temperature: Double,
    val timestamp: Long,
)