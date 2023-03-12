package com.mehbod.di

import com.mehbod.data.UserDataSource
import com.mehbod.data.ContactsDataSource
import com.mehbod.services.UserService
import com.mehbod.services.ContactsService
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    factory {
        val config: ApplicationConfig by inject()

        val jdbcUrl = config.property("storage.jdbcUrl").getString()
        val driverClassName = config.property("storage.driverClassName").getString()
        val user = config.property("storage.username").getString()
        val password = config.property("storage.password").getString()

        Database.connect(
            HikariDataSource().apply {
                this.driverClassName = driverClassName
                this.jdbcUrl = jdbcUrl
                this.username = user
                this.password = password

                maximumPoolSize = 3
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            }
        )
    }

    singleOf(::UserDataSource)
    singleOf(::ContactsDataSource)
    singleOf(::UserService)
    singleOf(::ContactsService)
}