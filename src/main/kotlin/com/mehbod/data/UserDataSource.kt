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

class UserDataSource(database: Database) {
    object Users : IntIdTable() {
        val username = varchar("username", length = 50).uniqueIndex()
        val passwordHash = text("password_hash")
    }

    class User(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<User>(Users)

        var username by Users.username
        var passwordHash by Users.passwordHash
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun addUser(username: String, passwordHash: String) = dbQuery {
        User.new {
            this.username = username
            this.passwordHash = passwordHash
        }
    }

    suspend fun fetchUser(username: String) = dbQuery {
        User.find { Users.username eq username }.firstOrNull()
    }

    suspend fun getAllUsers() = dbQuery {
        User.all().map { it.username }
    }
}