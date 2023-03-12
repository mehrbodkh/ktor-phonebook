package com.mehbod.plugins

import com.mehbod.data.UserDataSource
import com.mehbod.services.configureUserServiceRouting
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(AutoHeadResponse)
    configureUserServiceRouting()
    routing {
        val dao by inject<UserDataSource>()

        authenticate("auth-basic") {
            get("/allUsers") {
                call.respond(dao.getAllUsers())
            }
        }
    }
}
