package ua.nure.route.lamp

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.delete
import org.koin.ktor.ext.inject
import io.ktor.server.request.*
import io.ktor.server.response.*
import ua.nure.data.Error
import ua.nure.data.lamp.LampRepository
import ua.nure.data.lamp.dto.LampRequest

fun Route.deleteLamp() {
    delete("lamp/{lampId}") {
        val repository by application.inject<LampRepository>()

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