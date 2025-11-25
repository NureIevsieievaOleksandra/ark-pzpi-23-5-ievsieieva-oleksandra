package ua.nure.route

import io.ktor.server.application.*
import io.ktor.server.routing.routing
import ua.nure.route.group.createGroup
import ua.nure.route.group.deleteGroup
import ua.nure.route.group.getGroupById
import ua.nure.route.group.getGroups
import ua.nure.route.group.updateGroup
import ua.nure.route.lamp.createLamp
import ua.nure.route.lamp.deleteLamp
import ua.nure.route.lamp.getLampById
import ua.nure.route.lamp.getLamps
import ua.nure.route.lamp.updateLamp

fun Application.configureRouting() {
    routing {
        getLamps()
        createLamp()
        deleteLamp()
        updateLamp()
        getLampById()

        createGroup()
        deleteGroup()
        getGroupById()
        getGroups()
        updateGroup()
    }
}
