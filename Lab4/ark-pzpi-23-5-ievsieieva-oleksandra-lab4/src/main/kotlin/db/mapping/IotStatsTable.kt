package ua.nure.db.mapping

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import ua.nure.data.iotsocket.IotStatistics
import ua.nure.data.iotsocket.IotStatisticsReview
import ua.nure.data.iotsocket.IotTemperatureChart
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Instant

object IotStatsTable : IntIdTable(name = "iot_stats") {
    val lampId = integer(name = "lamp_id")
    val energyToday = double("energy")
    val lastEnergyUpdate = long("last_energy_update")
    val temperature = double("temperature")
    val uptime = long("uptime")
    val timestamp = long("timestamp")
}

class IotStatsDao(eventId: EntityID<Int>) : IntEntity(eventId) {
    companion object : IntEntityClass<IotStatsDao>(IotStatsTable)
    var lampId by IotStatsTable.lampId
    var energyToday by IotStatsTable.energyToday
    var lastEnergyUpdate by IotStatsTable.lastEnergyUpdate
    var temperature by IotStatsTable.temperature
    var uptime by IotStatsTable.uptime
    var timestamp by IotStatsTable.timestamp
}

fun IotStatsDao.daoToModel() = IotStatistics(
    id = this.id.value,
    lampId = this.lampId,
    energyToday = this.energyToday,
    lastEnergyUpdate = this.lastEnergyUpdate,
    temperature = this.temperature,
    uptime = this.uptime,
    timestamp = LocalDateTime.ofInstant(
        java.time.Instant.ofEpochMilli(this.timestamp),
        ZoneId.systemDefault()
    )
)

fun List<IotStatsDao>.toStatistics() = IotStatisticsReview(
    lampId = this.first().lampId,
    powerConsumption = this.last().energyToday,
    iotTemperatureChart = this.map { IotTemperatureChart(temperature = it.temperature, timestamp = it.timestamp) },
    uptime = this.last().uptime,
)