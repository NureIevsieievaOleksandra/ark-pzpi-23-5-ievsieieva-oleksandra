package ua.nure

import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    Database.connect(
        url = environment.config.property("storage.jdbcURL").getString(),
        driver = environment.config.property("storage.driverClassName").getString(),
        user = environment.config.property("storage.user").getString(),
//        password = System.getenv("PG_PASSWORD")
        password = "Secret1"
    )
}