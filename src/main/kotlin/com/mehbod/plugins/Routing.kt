package com.mehbod.plugins

import com.mehbod.data.UserRepository
import com.mehbod.model.User
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(AutoHeadResponse)

    val repository: UserRepository by inject()

    routing {
        route("/addUser") {
            post("/") {
                val user = call.receive<User>()
                repository.addUser(user)
                call.respond("Done")
            }
        }
        get("/allUsers") {
            val users = repository.getAllUsers()
            call.respond(users)
        }
    }
}
