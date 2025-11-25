package ua.nure.route.lamp

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.put
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.lamp.LampRepository
import ua.nure.data.lamp.dto.LampRequest
import ua.nure.data.lamp.mapper.toModel
import kotlin.getValue

fun Route.updateLamp() {
    put("lamp") {
        val repository by application.inject<LampRepository>()

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

        lamp?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: run {
            call.respond(
                status = HttpStatusCode.UnprocessableEntity,
                message = Error(message = "Something went wrong")
            )
        }
    }
}