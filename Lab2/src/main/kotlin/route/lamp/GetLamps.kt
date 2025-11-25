package ua.nure.route.lamp

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.lamp.LampRepository

fun Route.getLamps() {
    get("lamp") {
        val repository by application.inject<LampRepository>()

        val lamps = repository.getLamps()
        if(lamps.isEmpty()) {
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
}