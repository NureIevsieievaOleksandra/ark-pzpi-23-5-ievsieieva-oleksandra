package ua.nure.route.group

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.group.GroupRepository
import ua.nure.data.lamp.LampRepository
import kotlin.getValue

fun Route.getGroupById() {
    get("group/getById") {
        val repository by application.inject<GroupRepository>()

        val groupId = call.parameters["groupId"]?.runCatching { toInt() }?.getOrNull() ?: run {
            return@get call.respond(
                status = HttpStatusCode.BadRequest,
                message = Error(message = "Group bad request")
            )
        }

        val lamp = repository.getGroupById(groupId = groupId) ?: return@get call.respond(
            status = HttpStatusCode.NotFound,
            message = Error(message = "Group not found")
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = lamp
        )
    }
}