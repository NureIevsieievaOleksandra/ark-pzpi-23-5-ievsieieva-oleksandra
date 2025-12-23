package ua.nure.data.group

import io.ktor.server.engine.applicationEnvironment
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import ua.nure.db.mapping.GroupDao
import ua.nure.db.mapping.GroupTable
import ua.nure.db.mapping.daoToModel
import ua.nure.db.mapping.suspendTransaction

class PostgresGroupRepository : GroupRepository {
    override suspend fun getGroups(): List<Group> =
        suspendTransaction {
            GroupDao.all().map { it.daoToModel() }
        }

    override suspend fun create(group: Group): Group =
        suspendTransaction {
            val dao = GroupDao.new {
                name = group.name
                description = group.description
            }

            dao.daoToModel()
        }

    override suspend fun delete(groupId: Int): Unit = suspendTransaction {
        GroupTable.deleteWhere { GroupTable.id eq groupId }
    }

    override suspend fun update(group: Group): Unit = suspendTransaction {
        group.groupId?.let { id ->
            GroupTable.update({ GroupTable.id eq id }) {
                it[name] = group.name
                it[description] = group.description
            }
        }
    }

    override suspend fun getGroupById(groupId: Int): Group? = suspendTransaction {
        GroupDao.find { GroupTable.id eq groupId }
            .limit(1)
            .map { it.daoToModel() }
            .firstOrNull()
    }
}