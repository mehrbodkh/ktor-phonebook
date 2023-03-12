package com.mehbod.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    install(AutoHeadResponse)
    routing {
    }
}