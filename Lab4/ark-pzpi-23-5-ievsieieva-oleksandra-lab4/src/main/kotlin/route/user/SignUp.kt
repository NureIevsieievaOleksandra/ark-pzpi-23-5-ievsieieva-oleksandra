package route.user

import data.user.dto.SignUpRequest
import data.user.dto.SignUpResponse
import data.user.mapper.toDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject
import ua.nure.data.Error
import ua.nure.data.user.Role
import ua.nure.data.user.User
import ua.nure.data.user.UserRepository
import ua.nure.security.hashing.HashingService
import ua.nure.security.token.TokenClaim
import ua.nure.security.token.TokenConfig
import ua.nure.security.token.TokenService
import kotlin.getValue

fun Route.signUp() {
    post("signUp") {

        val hashingService by application.inject<HashingService>()
        val userRepository by application.inject<UserRepository>()
        val tokenConfig by application.inject<TokenConfig>()
        val tokenService by application.inject<TokenService>()

        val request = call.runCatching {
            receiveNullable<SignUpRequest>()
        }.getOrNull() ?: return@post call.respond(
            status = HttpStatusCode.BadRequest,
            message = Error(
                message = "Bad Request. Phase 1",
            )
        )

        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPasswordTooShort = request.password.length < 4

        if (isPasswordTooShort || areFieldsBlank) {
            return@post call.respond(
                status = HttpStatusCode.BadRequest,
                message = Error(
                    message = "Name or password must not be blank or password is too short"
                )
            )
        }

        val saltedHash = hashingService.generateSaltedHash(value = request.password)

        when(val user = userRepository.insertUser(
            user = User(
                name = request.username.lowercase(),
                password = saltedHash.hash,
                salt = saltedHash.salt,
            )
        )) {
            null -> call.respond(
                status = HttpStatusCode.Conflict,
                message = Error(
                    message = "User already exists",
                )
            )
            else ->{
                val token = tokenService.generate(
                    config = tokenConfig,
                    TokenClaim(
                        name = "role",
                        value = (Role.User).toString()
                    )
                )
                call.respond(
                    status = HttpStatusCode.OK,
                    message = SignUpResponse(
                        user = user.toDto(),
                        token = token
                    )
                )
            }
        }





    }
}