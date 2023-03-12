package com.mehbod.di

import com.mehbod.data.UserRepository
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val appModule = module {
    factory {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/phonebook_learning",
            user = "mehrbod",
            driver = "org.h2.Driver",
            password = ""
        )
    }

    single { UserRepository(get()) }
}