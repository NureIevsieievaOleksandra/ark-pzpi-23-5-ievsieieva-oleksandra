package route.user

import data.user.dto.SignInRequest
import data.user.dto.SignInResponse
import data.user.mapper.asDtoValue
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.user.Role
import ua.nure.data.user.UserRepository
import ua.nure.security.hashing.HashingService
import ua.nure.security.hashing.SaltedHash
import ua.nure.security.token.TokenClaim
import ua.nure.security.token.TokenConfig
import ua.nure.security.token.TokenService

fun Route.singIn() {
    post("signIn") {
        val userRepository by application.inject<UserRepository>()
        val hashingService by application.inject<HashingService>()
        val tokenConfig by application.inject<TokenConfig>()
        val tokenService by application.inject<TokenService>()

        val request = call.runCatching {
            receiveNullable<SignInRequest>()
        }.getOrNull() ?: run {
            return@post call.respond(
                status =HttpStatusCode.BadRequest,
                message = Error(message = "Bad Request")
            )
        }

        val user = userRepository.getUserByUserName(name = request.username.lowercase()) ?:
            return@post call.respond(
                status = HttpStatusCode.NotFound,
                message = Error(message = "User not found")
            )

        call.application.environment.log.error("Signed in user ${user.userId} -> ${user.role}")

        if(!hashingService.verify(value = request.password, saltedHash = SaltedHash(hash = user.password, salt = user.salt))) {
            return@post call.respond(
                status = HttpStatusCode.Unauthorized,
                message = Error(message = "Unauthorized")
            )
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "role",
                value = user.role.toString()
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = SignInResponse(
                token = token,
                userId = user.userId,
                userName = user.name,
                role = user.role.asDtoValue()
            )
        )
    }
}