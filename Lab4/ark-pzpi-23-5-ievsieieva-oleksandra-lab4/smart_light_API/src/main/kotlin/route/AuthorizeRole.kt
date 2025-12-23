package ua.nure.route

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.routing.Route
import io.ktor.server.routing.intercept
import ua.nure.data.Error
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelinePhase
import org.slf4j.LoggerFactory
import ua.nure.data.user.Role
import ua.nure.extension.toRole

fun RoleAuthorization() = createRouteScopedPlugin(
    name = "RoleAuthorization"
) {

    onCall { call ->
        val principal = call.principal<JWTPrincipal>()
        val role = principal?.getClaim(name = "role", String::class)?.toRole()
        val endpoint = call.request.path()
        val method = call.request.httpMethod.value.lowercase()

        when {
            endpoint.startsWith("/signUp") || endpoint.startsWith("/signIn") -> {
                return@onCall
            }

            role == null || role == Role.Undefined -> {
                call.respond(
                    status = HttpStatusCode.Forbidden,
                    message = Error(message = "Role not authorized: null or Undefined: $role")
                )
                return@onCall
            }

            endpoint.startsWith("/user") -> {
                if (role !in listOf(Role.Admin)) {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = Error(message = "Role not authorized: $method")
                    )
                    return@onCall
                }
            }


            endpoint.startsWith("/group") || endpoint.startsWith("/lamp") -> {
                when (method) {
                    "get",
                    "put" -> {
                        call.application.environment.log.info("$method, $endpoint, $role, ${role::class.qualifiedName}")
                        if (role !in listOf(Role.Admin, Role.User)) {
                            call.respond(
                                status = HttpStatusCode.Forbidden,
                                message = Error(message = "Role not authorized: /group, /lamp -> $endpoint, $role, $method")
                            )
                            return@onCall
                        }
                    }

                    "delete",
                    "post" -> {
                        if (role !in listOf(Role.Admin)) {
                            call.respond(
                                status = HttpStatusCode.Forbidden,
                                message = Error(message = "Role not authorized: $method")
                            )
                            return@onCall
                        }
                    }
                }
            }

            else -> {
                call.respond(
                    status = HttpStatusCode.Forbidden,
                    message = Error(message = "RoleAuthorization: other reason: $endpoint, $role, $method")
                )
                return@onCall
            }
        }

    }
}