package ua.nure.route.lamp

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.lamp.Lamp
import ua.nure.data.lamp.LampRepository
import ua.nure.db.mapping.suspendTransaction
import kotlin.getValue

fun Route.getLampById() {
    get("lamp/getById") {
        val repository by application.inject<LampRepository>()

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


}