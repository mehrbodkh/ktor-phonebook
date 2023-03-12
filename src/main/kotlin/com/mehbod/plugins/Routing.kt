package com.mehbod.plugins

import com.mehbod.services.configureContactsServiceRouting
import com.mehbod.services.configureUserServiceRouting
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*

fun Application.configureRouting() {
    install(AutoHeadResponse)
    configureUserServiceRouting()
    configureContactsServiceRouting()
}
