package ua.nure.db.mapping

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import ua.nure.data.user.Role
import ua.nure.data.user.User

object UserTable : IntIdTable(name = "user") {
    val name = varchar("name", 255)
    val password = varchar("password", 255)
    val salt = varchar("salt", 255)
    val role = enumerationByName<Role>("role", 20).nullable()
}

class UserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDao>(UserTable)
    var name by UserTable.name
    var password by UserTable.password
    var salt by UserTable.salt
    var role by UserTable.role
}

fun UserDao.daoToModel() = User(
    userId = id.value,
    name = name,
    password = password,
    salt = salt,
    role = role
)