package com.mehbod.di

import com.mehbod.data.UserDataSource
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val appModule = module {
    factory {
        val config: ApplicationConfig by inject()

        val host = config.property("postgres.host").getString()
        val port = config.property("postgres.port").getString()
        val dbName = config.property("postgres.db_name").getString()
        val user = config.property("postgres.username").getString()
        val password = config.property("postgres.password").getString()

        Database.connect(
            url = "jdbc:postgresql://$host:$port/$dbName",
            user = user,
            driver = "org.h2.Driver",
            password = password
        )
    }

    single { UserDataSource(get()) }
}