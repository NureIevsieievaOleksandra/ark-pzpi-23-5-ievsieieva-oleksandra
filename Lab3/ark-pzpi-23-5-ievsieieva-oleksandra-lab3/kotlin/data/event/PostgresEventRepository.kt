package ua.nure.data.event

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.times
import org.jetbrains.exposed.sql.avg
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.selectAll
import ua.nure.data.event.dto.ColorContest
import ua.nure.db.mapping.EventDao
import ua.nure.db.mapping.EventTable
import ua.nure.db.mapping.GroupDao
import ua.nure.db.mapping.LampDao
import ua.nure.db.mapping.daoToModel
import ua.nure.db.mapping.suspendTransaction
import java.time.ZoneId

class PostgresEventRepository : EventRepository {
    override suspend fun createEvent(event: Event): Event = suspendTransaction {
        val dao = EventDao.new {
            lampId = event.lampId?.let { LampDao.findById(it) }
            groupId = event.groupId?.let { GroupDao.findById(it) }
            r = event.r
            g = event.g
            b = event.b
            brightness = event.brightness
            active = event.active
            timestamp = event.date
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }
        dao.daoToModel()
    }

    override suspend fun getEvents(): List<Event> = suspendTransaction {
        EventDao.all().map { it.daoToModel() }
    }

    override suspend fun getByColors(): List<ColorContest> = suspendTransaction {
        EventTable
            .select(
                EventTable.r,
                EventTable.g,
                EventTable.b,
                EventTable.id.count()
            )
            .groupBy(
                EventTable.r,
                EventTable.g,
                EventTable.b
            )
            .orderBy(EventTable.id.count(), SortOrder.DESC)
            .limit(10)
            .map {
                ColorContest(
                    r = it[EventTable.r],
                    g = it[EventTable.g],
                    b = it[EventTable.b],
                    count = it[EventTable.id.count()]
                )
            }
    }

    override suspend fun getMathExpectationR(): Pair<Double, Double> =
        suspendTransaction {
            val meanR = EventTable.r.avg()
            val meanRSq = (EventTable.r * EventTable.r).avg()

            EventTable.slice(meanR, meanRSq)
                .selectAll()
                .singleOrNull()
                ?.let {
                    val mean = (it[meanR] as? Number)?.toDouble() ?: 0.0
                    val variance = ((it[meanRSq] as? Number)?.toDouble() ?: 0.0) - mean * mean
                    mean to variance
                } ?: (0.0 to 0.0)
        }

    override suspend fun getMathExpectationG(): Pair<Double, Double> =
        suspendTransaction {
            val meanR = EventTable.r.avg()
            val meanRSq = (EventTable.r * EventTable.r).avg()

            EventTable.slice(meanR, meanRSq)
                .selectAll()
                .singleOrNull()
                ?.let {
                    val mean = (it[meanR] as? Number)?.toDouble() ?: 0.0
                    val variance = ((it[meanRSq] as? Number)?.toDouble() ?: 0.0) - mean * mean
                    mean to variance
                } ?: (0.0 to 0.0)
        }

    override suspend fun getMathExpectationB(): Pair<Double, Double> =
        suspendTransaction {
            val meanR = EventTable.r.avg()
            val meanRSq = (EventTable.r * EventTable.r).avg()

            EventTable.slice(meanR, meanRSq)
                .selectAll()
                .singleOrNull()
                ?.let {
                    val mean = (it[meanR] as? Number)?.toDouble() ?: 0.0
                    val variance = ((it[meanRSq] as? Number)?.toDouble() ?: 0.0) - mean * mean
                    mean to variance
                } ?: (0.0 to 0.0)
        }
}