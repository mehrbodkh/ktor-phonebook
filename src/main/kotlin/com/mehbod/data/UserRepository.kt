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
import com.mehbod.model.User as UserModel

class UserRepository(database: Database) {
    object Users : IntIdTable() {
        val username = varchar("username", length = 50)
        val password = text("password")
    }

    class User(id: EntityID<Int>) : IntEntity(id) {
        companion object: IntEntityClass<User>(Users)
        var username by Users.username
        var password by Users.password
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }


    suspend fun addUser(user: UserModel) {
        dbQuery {
            User.new {
                username = user.username
                password = user.password
            }
        }
    }

    suspend fun getAllUsers() = dbQuery {
        User.all().map {
            UserModel(it.username, it.password)
        }
    }
}