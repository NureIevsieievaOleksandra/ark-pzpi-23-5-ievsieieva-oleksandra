package ua.nure.data.iotsocket

import ua.nure.db.mapping.IotStatsDao
import ua.nure.db.mapping.daoToModel
import ua.nure.db.mapping.suspendTransaction
import ua.nure.db.mapping.toStatistics
import java.time.ZoneId

class PostgresIotStatisticsRepository : IotStatisticsRepository {
    override suspend fun create(iotStatistics: IotStatistics): IotStatistics = suspendTransaction {
        IotStatsDao.new {
            lampId = iotStatistics.lampId
            energyToday = iotStatistics.energyToday
            lastEnergyUpdate = iotStatistics.lastEnergyUpdate
            temperature = iotStatistics.temperature
            uptime = iotStatistics.uptime
            timestamp = iotStatistics.timestamp
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }.daoToModel()
    }

    override suspend fun get(): Map<Int, IotStatisticsReview> = suspendTransaction {
        IotStatsDao
            .all()
            .groupBy { it.lampId }
            .map { item: Map.Entry<Int, List<IotStatsDao>> ->
                item.key to item.value.sortedBy { it.timestamp }
            }
            .toMap()
            .map { item: Map.Entry<Int, List<IotStatsDao>> ->
                item.key to item.value.toStatistics()
            }.toMap()

    }
}