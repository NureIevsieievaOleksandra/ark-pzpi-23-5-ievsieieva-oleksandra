package ua.nure.route.group

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.group.Group
import ua.nure.data.group.GroupRepository
import ua.nure.data.group.dto.GroupRequest
import ua.nure.data.group.mapper.toModel
import ua.nure.data.user.Role
import ua.nure.route.isGroupNotValid
import kotlin.getValue

fun Route.routeGroup() {
    authenticate {
        route("/group") {

            val repository by application.inject<GroupRepository>()

            get {
                if(call.isGroupNotValid(enabledRoles = listOf(Role.Admin, Role.User))) {
                    return@get call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val groups = repository.getGroups()
                if (groups.isEmpty()) {
                    return@get call.respond(
                        status = HttpStatusCode.NotFound,
                        message = Error(message = "Groups not found")
                    )
                }
                call.respond(
                    status = HttpStatusCode.OK,
                    message = groups
                )
            }

            get("/getById") {
                if(call.isGroupNotValid(enabledRoles = listOf(Role.Admin, Role.User))) {
                    return@get call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val groupId = call.request.queryParameters["groupId"]?.runCatching { toInt() }?.getOrNull() ?: run {
                    return@get call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Error(message = "Group bad request")
                    )
                }

                val group = repository.getGroupById(groupId = groupId) ?: return@get call.respond(
                    status = HttpStatusCode.NotFound,
                    message = Error(message = "Group not found")
                )

                call.respond(
                    status = HttpStatusCode.OK,
                    message = group
                )
            }

            post {
                if(call.isGroupNotValid(enabledRoles = listOf(Role.Admin))) {
                    return@post call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }
                val request = call.runCatching {
                    receiveNullable<GroupRequest>()
                }.getOrNull() ?: run {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Error(message = "Bad Request")
                    )
                    return@post
                }

                call.respond(
                    status = HttpStatusCode.OK,
                    message = repository.create(
                        group = Group(
                            name = request.name,
                            description = request.description,
                        )
                    )
                )
            }


            put {
                if(call.isGroupNotValid(enabledRoles = listOf(Role.Admin, Role.User))) {
                    return@put call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val request = call.runCatching {
                    receiveNullable<GroupRequest>()
                }.getOrNull() ?: run {
                    return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Error(message = "Bad Request")
                    )
                }

                val group = request.toModel()
                repository.update(group = group)

                call.respond(
                    status = HttpStatusCode.OK,
                    message = group
                )
            }

            delete("/{groupId}") {
                if(call.isGroupNotValid(enabledRoles = listOf(Role.Admin))) {
                    return@delete call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val groupId = call.parameters["groupId"]?.runCatching { toInt() }?.getOrNull() ?: run {
                    return@delete call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Error(message = "Group bad request")
                    )
                }

                repository.delete(groupId = groupId)

                call.respond(
                    status = HttpStatusCode.NoContent,
                    message = "Group deleted successfully"
                )
            }

        }
    }
}