package ua.nure.di

import io.ktor.server.config.ApplicationConfig
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ua.nure.data.event.EventRepository
import ua.nure.data.event.PostgresEventRepository
import ua.nure.data.group.GroupRepository
import ua.nure.data.group.PostgresGroupRepository
import ua.nure.data.lamp.LampRepository
import ua.nure.data.lamp.PostgresLampRepository
import ua.nure.data.user.PostgresUserRepository
import ua.nure.data.user.UserRepository
import ua.nure.security.hashing.HashingService
import ua.nure.security.hashing.SHA256HashingService
import ua.nure.security.token.JwtTokenService
import ua.nure.security.token.TokenConfig
import ua.nure.security.token.TokenService

fun appModule(
    config: ApplicationConfig
) = module {
    single { config }

    singleOf(::PostgresLampRepository) { bind<LampRepository>() }
    singleOf(::PostgresGroupRepository) { bind<GroupRepository>() }
    singleOf(::PostgresUserRepository) {bind<UserRepository>() }
    singleOf(::PostgresEventRepository) { bind<EventRepository>() }


    single<TokenConfig> {
        TokenConfig(
            issuer = config.property("jwt.issuer").getString(),
            audience = config.property("jwt.audience").getString(),
            expiresIn = 365L * 1000L * 60L * 60L * 24L,
            secret = System.getenv("JWT_SECRET")
        )
    }

    singleOf(::JwtTokenService){ bind<TokenService>() }
    singleOf(::SHA256HashingService) { bind<HashingService>()}
}