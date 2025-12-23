package ua.nure.data.iotsocket.dto

import kotlinx.serialization.Serializable
import ua.nure.data.iotsocket.IotStatistics
import java.time.LocalDateTime
import java.time.ZoneOffset

@Serializable
data class IotStatisticsDto(
    val id: Int,
    val energy: EnergyDto,
    val last_reset: String,
    val temperature: Double,
    val uptime: Long
)

@Serializable
data class EnergyDto(
    val energy_today_mwh: Double,
    val last_update_ms: Long,
)

fun IotStatisticsDto.toModel() = IotStatistics(
    id = id,
    lampId = id,
    energyToday = energy.energy_today_mwh,
    lastEnergyUpdate = energy.last_update_ms,
    temperature = temperature,
    uptime = uptime,
    timestamp = LocalDateTime.now()

)
