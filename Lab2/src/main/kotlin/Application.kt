package ua.nure

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ua.nure.di.appModule
import ua.nure.route.configureRouting

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(
            appModule(
                config = environment.config
            )
        )

    }
    configureSerialization()
    configureDatabase()
    configureRouting()
}
