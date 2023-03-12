package com.mehbod.services

import com.mehbod.data.ContactsDataSource
import com.mehbod.model.Contact
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureContactsServiceRouting() = routing {
    val service: ContactsService by inject()

    authenticate("auth-basic") {
        post("/addContact") {
            val request = call.receive<Contact>()
            val username = call.principal<UserIdPrincipal>()!!.name
            service.addContact(request, username)
            call.respond("OK")
        }

        get("/contacts") {
            val username = call.principal<UserIdPrincipal>()!!.name
            service.getAllContacts(username)?.let {
                call.respond(it)
            } ?: run {
                call.respond("Failed")
            }
        }
    }
}

class ContactsService(private val contactsDataSource: ContactsDataSource, private val userService: UserService) {
    suspend fun addContact(contact: Contact, username: String) {
        userService.findUser(username)?.let {
            contactsDataSource.addContact(contact, it)
        }
    }

    suspend fun getAllContacts(username: String): List<Contact>? = userService.findUser(username)?.let {
        contactsDataSource.fetchContacts(it)
    }
}