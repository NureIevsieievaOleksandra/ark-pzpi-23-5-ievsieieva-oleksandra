package ua.nure.route.analytics

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.event.EventRepository
import ua.nure.data.event.dto.AnalyticsDto
import ua.nure.data.user.Role
import ua.nure.route.isGroupNotValid
import kotlin.getValue

fun Route.analyticsRoute() {
    authenticate {
        route("/analytics") {
            val eventRepository by application.inject<EventRepository>()

            get {
                if (call.isGroupNotValid(enabledRoles = listOf(Role.Admin, Role.User))) {
                    return@get call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(
                            message = "Role not authorized"
                        )
                    )
                }

                val colorContest = eventRepository.getByColors()
                val (mathExp, variance) = eventRepository.getMathExpectationR()

                val analyticsDto = AnalyticsDto(
                    colorContest = colorContest,
                    mathExpectationR = mathExp,
                    mathExpectationG = mathExp,
                    mathExpectationB = mathExp,
                    varianceR = variance,
                    varianceG = variance,
                    varianceB = variance
                )

                call.respond(analyticsDto)

            }
        }
    }
}