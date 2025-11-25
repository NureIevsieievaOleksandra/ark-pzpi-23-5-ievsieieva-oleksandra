package ua.nure.route.group

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.group.Group
import ua.nure.data.group.GroupRepository
import ua.nure.data.group.dto.GroupRequest
import ua.nure.data.lamp.Lamp
import ua.nure.data.lamp.LampRepository
import ua.nure.data.lamp.dto.LampRequest
import kotlin.getValue

fun Route.createGroup() {
    post("group") {
        val repository by application.inject<GroupRepository>()

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
}