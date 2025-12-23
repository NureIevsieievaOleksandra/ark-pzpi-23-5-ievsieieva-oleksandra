package ua.nure.data

import io.ktor.websocket.WebSocketSession
import java.util.Collections

object WsSessions {
    val sessions = Collections.synchronizedSet<WebSocketSession>(LinkedHashSet())
}