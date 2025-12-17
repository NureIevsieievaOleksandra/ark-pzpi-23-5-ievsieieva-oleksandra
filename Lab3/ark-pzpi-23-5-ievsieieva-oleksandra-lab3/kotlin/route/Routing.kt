package ua.nure.route

import io.ktor.server.application.*
import io.ktor.server.routing.routing
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.util.reflect.TypeInfo
import io.ktor.websocket.Frame
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import route.user.signUp
import route.user.singIn
import ua.nure.data.WsSessions
import ua.nure.data.lamp.dto.LampCommandDto
import ua.nure.route.analytics.analyticsRoute
import ua.nure.route.group.routeGroup
import ua.nure.route.lamp.lampRoute
import ua.nure.route.user.userRoute

fun Application.configureRouting() {
    routing {
        routeGroup()
        lampRoute()

        singIn()
        signUp()
        userRoute()
        analyticsRoute()

        webSocket("/ws") {
            WsSessions.sessions += this
            try {
                incoming.consumeEach {}

            } finally {
                WsSessions.sessions -= this
            }
        }
    }
}
