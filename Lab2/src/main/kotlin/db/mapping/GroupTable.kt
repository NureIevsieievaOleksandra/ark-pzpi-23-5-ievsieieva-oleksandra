package ua.nure.db.mapping

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import ua.nure.data.group.Group

object GroupTable : IntIdTable(name = "group") {
    val name = varchar(name = "name", length = 50).nullable()
    val description = varchar(name = "description", length = 255).nullable()
}

class GroupDao(groupId: EntityID<Int>) : IntEntity(groupId) {
    companion object : IntEntityClass<GroupDao>(GroupTable)
    var name by GroupTable.name
    var description by GroupTable.description
    val lamps by LampDao referrersOn LampTable
}

fun GroupDao.daoToModel() = Group(
    groupId = id.value,
    name = name,
    description = description,
    lamps = lamps.map { it.daoToModel() }

)