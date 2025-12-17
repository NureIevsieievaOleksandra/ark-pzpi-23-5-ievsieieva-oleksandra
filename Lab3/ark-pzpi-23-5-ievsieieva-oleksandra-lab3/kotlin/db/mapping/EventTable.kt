package ua.nure.db.mapping

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.and
import ua.nure.data.event.Event
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object EventTable: IntIdTable(name = "event") {
    val lampId = reference("lampid", LampTable).nullable()
    val groupId = reference("groupid", GroupTable).nullable()
    val r = integer(name = "r").check { it greaterEq 0 and (it lessEq 255) }.nullable()
    val g = integer(name = "g").check { it greaterEq 0 and (it lessEq 255) }.nullable()
    val b = integer(name = "b").check { it greaterEq 0 and (it lessEq 255) }.nullable()
    val brightness = integer(name = "brightness").check { it greaterEq 0 and (it lessEq 255) }.nullable()
    val active = bool(name = "active").default(false)
    val timestamp = long(name = "timestamp")
}

class EventDao(eventId: EntityID<Int>) : IntEntity(eventId) {
    companion object : IntEntityClass<EventDao>(EventTable)
    var lampId by LampDao optionalReferencedOn  EventTable.lampId
    var groupId by GroupDao optionalReferencedOn EventTable.groupId
    var r by EventTable.r
    var g by EventTable.g
    var b by EventTable.b
    var brightness by EventTable.brightness
    var active by EventTable.active
    var timestamp by EventTable.timestamp
}

fun EventDao.daoToModel() = Event(
    eventId = this.id.value,
    lampId = this.lampId?.id?.value,
    groupId = this.groupId?.id?.value,
    r = this.r,
    g = this.g,
    b = this.b,
    brightness = this.brightness,
    active = this.active,
    date = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this.timestamp),
        ZoneId.systemDefault()
    )
)
