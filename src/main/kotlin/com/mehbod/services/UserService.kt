package com.mehbod.services

import com.mehbod.data.UserDataSource
import com.mehbod.model.User
import com.mehbod.util.hash
import com.mehbod.util.verify
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

data class SignUpResponse(
    val user: User? = null,
    val error: String? = null,
)

fun Application.configureUserServiceRouting() = routing {
    val service: UserService by inject()

    post("/signup") {
        val user = call.receive<User>()
        val response = service.signUp(user)
        call.respond(response)
    }

    post("/login") {
        val user = call.receive<User>()
        val response = service.login(user)
        call.respond(response)
    }
}

class UserService(
    private val userDataSource: UserDataSource
) {

    suspend fun signUp(user: User): SignUpResponse {
        return if (userDataSource.fetchUser(user.username) != null) {
            SignUpResponse(error = "Username already taken.")
        } else {
            val hashedPassword = hash(user.password)
            val result = userDataSource.addUser(user.username, hashedPassword)
            SignUpResponse(user = User(result.username, result.passwordHash))
        }
    }

    suspend fun canLogin(user: User): Boolean {
        return userDataSource.fetchUser(user.username)?.let {
            val verified = verify(user.password, it.passwordHash)
            verified
        } ?: run {
            false
        }
    }

    suspend fun login(user: User): SignUpResponse {
        return userDataSource.fetchUser(user.username)?.let {
            if (verify(user.password, it.passwordHash)) {
                SignUpResponse(user = user)
            } else {
                SignUpResponse(error = "Username or password is wrong.")
            }
        } ?: run {
            SignUpResponse(error = "Username or password is wrong.")
        }
    }

    suspend fun findUser(username: String) = userDataSource.fetchUser(username)
}