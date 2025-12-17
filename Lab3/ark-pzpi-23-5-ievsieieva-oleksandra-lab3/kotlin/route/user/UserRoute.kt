package ua.nure.route.user

import data.user.dto.UpdateUserRequest
import data.user.mapper.toDto
import data.user.mapper.toRole
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.group.GroupRepository
import ua.nure.data.user.Role
import ua.nure.data.user.UserRepository
import ua.nure.route.RoleAuthorization
import ua.nure.route.isGroupNotValid
import kotlin.getValue

fun Route.userRoute() {
    authenticate {
        route("/user") {

            val userRepository by application.inject<UserRepository>()

            get {
                if(call.isGroupNotValid(enabledRoles = listOf(Role.Admin))) {
                    return@get call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val users = userRepository.getAllUsers()

                if (users.isEmpty()) {
                    return@get call.respond(
                        status = HttpStatusCode.NotFound,
                        message = Error(
                            message = "Users not found",
                        )
                    )
                }

                call.respond(
                    status = HttpStatusCode.OK,
                    message = users.map {
                        it.toDto()
                    }
                )

            }

            put {
                if(call.isGroupNotValid(enabledRoles = listOf(Role.Admin))) {
                    return@put call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }
                val request = call.runCatching {
                    receiveNullable<UpdateUserRequest>()
                }.getOrNull() ?: run {
                    return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Error(
                            message = "Bad request"
                        )
                    )
                }

                val role = request.role.runCatching { toRole() }.getOrNull() ?: return@put call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = Error(message = "Invalid role")
                )

                when (val user = userRepository.updateUser(
                    userId = request.userId,
                    role = role
                )) {
                    null -> call.respond(
                        status = HttpStatusCode.NotFound,
                        message = Error(message = "User not found")
                    )

                    else -> call.respond(
                        status = HttpStatusCode.OK,
                        message = call.respond(
                            status = HttpStatusCode.OK,
                            message = user.toDto()
                        )
                    )
                }
            }

            delete("/{userId}") {
                if(call.isGroupNotValid(enabledRoles = listOf(Role.Admin))) {
                    return@delete call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val userId = call.parameters["userId"]?.runCatching { toInt() }?.getOrNull() ?: return@delete call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = Error("Param userId missing")
                )

                val user = userRepository.getUserById(userId)
                if(user?.role == Role.Admin) {
                    return@delete call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Error("Admin user")
                    )
                } else {
                    userRepository.deleteUserById(userId = userId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}