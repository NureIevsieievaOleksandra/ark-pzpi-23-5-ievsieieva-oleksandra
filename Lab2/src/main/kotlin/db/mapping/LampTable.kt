package ua.nure.db.mapping

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.and
import ua.nure.data.lamp.Lamp

object LampTable : IntIdTable(name = "lamp") {
    val name = varchar(name = "name", length = 50).nullable()
    val groupId = reference("groupId",GroupTable).nullable()
    val description = varchar(name = "description", length = 255).nullable()
    val r = integer(name = "r").check { it greaterEq 0 and (it lessEq 255) }.nullable()
    val g = integer(name = "g").check { it greaterEq 0 and (it lessEq 255) }.nullable()
    val b = integer(name = "b").check { it greaterEq 0 and (it lessEq 255) }.nullable()
    val brightness = integer(name = "brightness").check { it greaterEq 0 and (it lessEq 255) }.nullable()
    val active = bool(name = "active").default(false)
}

class LampDao(lampId: EntityID<Int>) : IntEntity(lampId) {
    companion object : IntEntityClass<LampDao>(LampTable)
    var name by LampTable.name
    var groupId by GroupDao optionalReferencedOn LampTable.groupId
    var description by LampTable.description
    var r by LampTable.r
    var g by LampTable.g
    var b by LampTable.b
    var brightness by LampTable.brightness
    var active by LampTable.active
}

fun LampDao.daoToModel() = Lamp(
    lampId = id.value,
    name = name,
    groupId = groupId?.id?.value,
    description = description,
    r = r,
    g = g,
    b = b,
    brightness = brightness,
    active = active,
)