package ua.nure.route.group

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.group.GroupRepository
import ua.nure.data.group.dto.GroupRequest
import ua.nure.data.group.mapper.toModel
import ua.nure.data.lamp.LampRepository
import ua.nure.data.lamp.dto.LampRequest
import ua.nure.data.lamp.mapper.toModel
import kotlin.getValue

fun Route.updateGroup() {
    put("group") {
        val repository by application.inject<GroupRepository>()

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
}