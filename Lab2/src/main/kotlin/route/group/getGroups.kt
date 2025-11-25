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

fun Route.getGroups() {
    get("group") {
        val repository by application.inject<GroupRepository>()

        val groups = repository.getGroups()
        if(groups.isEmpty()) {
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
}