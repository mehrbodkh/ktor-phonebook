package com.mehbod

import com.mehbod.di.configureKoin
import io.ktor.server.application.*
import com.mehbod.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureKoin()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
