package ua.nure.route.websocket

import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import ua.nure.data.WsSessions
import ua.nure.data.iotsocket.IotStatisticsRepository
import ua.nure.data.iotsocket.dto.IotStatisticsDto
import ua.nure.data.iotsocket.dto.toModel

fun Route.iotWebsocket() {
    webSocket("/ws") {
        val iotStatisticsRepository by application.inject<IotStatisticsRepository>()

        WsSessions.sessions += this
        try {
            incoming.consumeEach { frame ->
                when (frame) {
                    is Frame.Binary -> {}
                    is Frame.Close -> {}
                    is Frame.Ping -> {}
                    is Frame.Pong -> {}
                    is Frame.Text -> {
                        val text = frame.readText()
                        val stat = Json.decodeFromString(IotStatisticsDto.serializer(), text)
                        environment.log.info("websocket: $stat")
                        iotStatisticsRepository.create(stat.toModel())
                    }
                }
            }

        } finally {
            WsSessions.sessions -= this
        }
    }
}