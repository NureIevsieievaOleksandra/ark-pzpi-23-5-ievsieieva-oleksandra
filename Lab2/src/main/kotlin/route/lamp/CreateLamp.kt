package ua.nure.route.lamp

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject
import ua.nure.data.lamp.LampRepository
import ua.nure.data.lamp.dto.LampRequest
import io.ktor.server.request.*
import io.ktor.server.response.*
import ua.nure.data.Error
import ua.nure.data.lamp.Lamp
import ua.nure.data.lamp.mapper.toModel

fun Route.createLamp() {
    post("lamp") {
        val repository by application.inject<LampRepository>()

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
}