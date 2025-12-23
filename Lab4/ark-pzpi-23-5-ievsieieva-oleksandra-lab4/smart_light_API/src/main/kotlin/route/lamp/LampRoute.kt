package ua.nure.route.lamp

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
import io.ktor.websocket.Frame
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.WsSessions
import ua.nure.data.event.Event
import ua.nure.data.event.EventRepository
import ua.nure.data.lamp.LampRepository
import ua.nure.data.lamp.dto.LampCommandDto
import ua.nure.data.lamp.dto.LampRequest
import ua.nure.data.lamp.mapper.toModel
import ua.nure.data.user.Role
import ua.nure.route.RoleAuthorization
import ua.nure.route.isGroupNotValid
import java.time.LocalDateTime
import kotlin.getValue

fun Route.lampRoute() {
    authenticate {
        route("/lamp") {

            val repository by application.inject<LampRepository>()
            val eventRepository by application.inject<EventRepository>()

            get {
                if (call.isGroupNotValid(enabledRoles = listOf(Role.Admin, Role.User))) {
                    return@get call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val lamps = repository.getLamps()
                if (lamps.isEmpty()) {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = Error(message = "Lamp not found")
                    )
                    return@get
                }
                call.respond(
                    status = HttpStatusCode.OK,
                    message = lamps
                )
            }

            get("/getById") {
                if (call.isGroupNotValid(enabledRoles = listOf(Role.Admin, Role.User))) {
                    return@get call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val lampId = call.parameters["lampId"]?.runCatching { toInt() }?.getOrNull() ?: run {
                    return@get call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Error(message = "Lamp bad request")
                    )
                }

                val lamp = repository.getLampById(id = lampId) ?: return@get call.respond(
                    status = HttpStatusCode.NotFound,
                    message = Error(message = "Lamp not found")
                )

                call.respond(
                    status = HttpStatusCode.OK,
                    message = lamp
                )
            }

            post {
                if (call.isGroupNotValid(enabledRoles = listOf(Role.Admin))) {
                    return@post call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val request = call.runCatching {
                    receiveNullable<LampRequest>()
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
                        lamp = request.toModel()
                    )
                )
            }

            put {
                if (call.isGroupNotValid(enabledRoles = listOf(Role.Admin, Role.User))) {
                    return@put call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val request = call.runCatching {
                    receiveNullable<LampRequest>()
                }.getOrNull() ?: run {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Error(message = "Bad Request")
                    )
                    return@put
                }

                val lamp = repository.update(lamp = request.toModel())

                eventRepository.createEvent(
                    event = Event(
                        lampId = lamp?.lampId,
                        groupId = lamp?.groupId,
                        r = lamp?.r,
                        g = lamp?.g,
                        b = lamp?.b,
                        brightness = lamp?.brightness,
                        active = lamp?.active ?: false,
                        date = LocalDateTime.now(),
                    )
                )

                WsSessions.sessions.forEach { session ->
                    session.send(
                        Frame.Text(
                            Json.encodeToString(
                                LampCommandDto(
                                    r = lamp?.r ?: 0,
                                    g = lamp?.g ?: 0,
                                    b = lamp?.b ?: 0,
                                    brightness = lamp?.brightness ?: 0,
                                )
                            )
                        )
                    )
                }


                lamp?.let {
                    call.respond(HttpStatusCode.OK, it)
                } ?: run {
                    call.respond(
                        status = HttpStatusCode.UnprocessableEntity,
                        message = Error(message = "Something went wrong")
                    )
                }
            }

            delete("/{lampId}") {
                if (call.isGroupNotValid(enabledRoles = listOf(Role.Admin))) {
                    return@delete call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val lampId = call.parameters["lampId"]?.runCatching { toInt() }?.getOrNull() ?: run {
                    return@delete call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Error(message = "Lamp bad request")
                    )
                }

                repository.delete(lampId = lampId)

                call.respond(
                    status = HttpStatusCode.NoContent,
                    message = "Lamp deleted successfully"
                )
            }
        }
    }
}