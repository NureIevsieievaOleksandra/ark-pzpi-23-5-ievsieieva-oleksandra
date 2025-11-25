package ua.nure.di

import io.ktor.server.config.ApplicationConfig
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ua.nure.data.group.GroupRepository
import ua.nure.data.group.PostgresGroupRepository
import ua.nure.data.lamp.LampRepository
import ua.nure.data.lamp.PostgresLampRepository
import java.sql.Connection
import java.sql.DriverManager

fun appModule(
    config: ApplicationConfig
) = module {
    single { config }

    singleOf(::PostgresLampRepository) { bind<LampRepository>() }
    singleOf(::PostgresGroupRepository) { bind<GroupRepository>() }
}