package ua.nure.data.user

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import ua.nure.db.mapping.UserDao
import ua.nure.db.mapping.UserTable
import ua.nure.db.mapping.daoToModel
import ua.nure.db.mapping.suspendTransaction
import kotlin.collections.map

class PostgresUserRepository : UserRepository {
    override suspend fun getUserById(userId: Int): User? =
        suspendTransaction {
            UserDao.findById(userId)?.daoToModel()
        }

    override suspend fun getUserByUserName(name: String): User? =
        suspendTransaction {
            UserDao.find { UserTable.name eq name }
                .limit(1)
                .map { it.daoToModel()}
                .firstOrNull()
        }

    override suspend fun insertUser(user: User): User? {
        getUserByUserName(user.name)?.let {
            return null
        }

        return suspendTransaction {
            UserDao.new {
                name = user.name
                password = user.password
                salt = user.salt
                role = user.role
            }.daoToModel()
        }
    }


    override suspend fun updateUser(userId: Int, role: Role): User? {
        suspendTransaction {
            UserTable.update(where = { UserTable.id eq userId } ) {
                it[UserTable.role] = role
            }
        }
        return getUserById(userId = userId)
    }

    override suspend fun deleteUserById(userId: Int) {
        UserTable.deleteWhere { UserTable.id eq userId }
    }

    override suspend fun getAllUsers(): List<User> =
        suspendTransaction {
            UserDao.all().map { it.daoToModel() }
        }
}