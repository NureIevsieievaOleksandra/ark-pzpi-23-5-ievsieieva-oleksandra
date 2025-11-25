package ua.nure.route.group

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.group.GroupRepository
import ua.nure.data.lamp.LampRepository
import kotlin.getValue

fun Route.deleteGroup() {
    delete("group/{groupId}") {
        val repository by application.inject<GroupRepository>()

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