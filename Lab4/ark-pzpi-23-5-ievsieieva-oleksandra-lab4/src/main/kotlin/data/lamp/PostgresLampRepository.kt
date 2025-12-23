package ua.nure.data.lamp

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import ua.nure.db.mapping.GroupDao
import ua.nure.db.mapping.LampDao
import ua.nure.db.mapping.LampTable
import ua.nure.db.mapping.daoToModel
import ua.nure.db.mapping.suspendTransaction

class PostgresLampRepository : LampRepository {
    override suspend fun getLamps(): List<Lamp> =
        suspendTransaction {
            LampDao.all().map { it.daoToModel() }
        }

    override suspend fun create(lamp: Lamp): Lamp = suspendTransaction {
        val dao = LampDao.new {
            name = lamp.name
            groupId = lamp.groupId?.let { GroupDao.findById(it) }
            description = lamp.description
            r = lamp.r
            g = lamp.g
            b = lamp.b
            brightness = lamp.brightness
            active = lamp.active
        }

        dao.daoToModel()
    }

    override suspend fun delete(lampId: Int): Unit = suspendTransaction {
        LampTable.deleteWhere { LampTable.id eq lampId }
    }

    override suspend fun update(lamp: Lamp): Lamp? {
        suspendTransaction {
            lamp.lampId?.let { id ->
                LampTable.update({ LampTable.id eq id }) {
                    it[LampTable.name] = lamp.name
                    it[LampTable.groupId] = lamp.groupId?.let { GroupDao.findById(it) }?.id
                    it[LampTable.description] = lamp.description
                    it[LampTable.r] = lamp.r
                    it[LampTable.g] = lamp.g
                    it[LampTable.b] = lamp.b
                    it[LampTable.brightness] = lamp.brightness
                    it[LampTable.active] = lamp.active
                }
            }
        }
        return lamp.lampId?.let { getLampById(id = it) }
    }

    override suspend fun getLampById(id: Int): Lamp? = suspendTransaction {
        LampDao.find { LampTable.id eq id }
            .limit(1)
            .map { it.daoToModel() }
            .firstOrNull()
    }
}
