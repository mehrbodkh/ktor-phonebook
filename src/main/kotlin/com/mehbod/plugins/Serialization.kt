package com.mehbod.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
        gson {
            }
    }
    routing {
    }
}
