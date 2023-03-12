package com.mehbod.data

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import com.mehbod.model.Contact as ContactModel

class ContactsDataSource(database: Database) {
    object Contacts : IntIdTable() {
        val firstName = varchar("first_name", 50)
        val lastName = varchar("last_name", 50).nullable()
        val email = text("email").nullable()
        val phoneNumber = varchar("phone_number", 20).nullable()
        val userId = reference("user_id", UserDataSource.Users)
    }

    class Contact(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<Contact>(Contacts)
        var firstName by Contacts.firstName
        var lastName by Contacts.lastName
        var email by Contacts.email
        var phoneNumber by Contacts.phoneNumber
        var userId by UserDataSource.User referencedOn Contacts.userId
    }

    init {
        transaction(database) {
            SchemaUtils.create(Contacts)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun addContact(contact: ContactModel, user: UserDataSource.User) = dbQuery {
        Contact.new {
            this.firstName = contact.firstName
            this.lastName = contact.lastName
            this.email = contact.email
            this.phoneNumber = contact.phoneNumber
            this.userId = user
        }
    }

    suspend fun fetchContacts(user: UserDataSource.User): List<ContactModel> = dbQuery {
        Contact.find { Contacts.userId eq user.id }.map {
            ContactModel(
                it.firstName,
                it.lastName,
                it.email,
                it.phoneNumber
            )
        }
    }
}