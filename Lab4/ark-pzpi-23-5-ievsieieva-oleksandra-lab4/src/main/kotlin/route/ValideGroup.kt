package ua.nure.route

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.RoutingCall
import ua.nure.data.user.Role
import ua.nure.extension.toRole
import kotlin.collections.contains

fun ApplicationCall.isGroupNotValid(
    enabledRoles: List<Role>,
): Boolean =
    principal<JWTPrincipal>()?.getClaim(name = "role", String::class)?.toRole() !in enabledRoles

