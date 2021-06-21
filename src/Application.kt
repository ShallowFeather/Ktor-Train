package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.engine.jetty.*
import java.util.*
import com.example.database.PasswordHasher
import com.example.database.*
import io.ktor.sessions.*
import org.jetbrains.exposed.sql.Database.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class User(
    var Name: String,
    var Password: String
)

fun Application.module() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT) // 美化输出 JSON
        }
    }

    install(Sessions) {
        cookie<UserIdPrincipal>(
            "login_data",
            storage = SessionStorageMemory()
        ) {
            cookie.path = "/"
        }
    }

    data class UserIdAuthorityPrincipal(val userId: String) : Principal
    install(Authentication) {
        session<UserIdAuthorityPrincipal>("Session Auth") {
            challenge {
                throw BadRequestException("Authentication Error.")
            }
            validate { session: UserIdAuthorityPrincipal ->
                session
            }
        }
    }

    initDatabase()
    routing {

        post("/login") {
            val userLoginDTO = call.receive<User>()
            var userId: String? = null
            transaction {
                val userData = UserAccount.select { UserAccount.UserName.eq(userLoginDTO.Name) }.firstOrNull()

                if (userData == null) throw BadRequestException("Authentication Error.")
                if (!PasswordHasher.verifyPassword.verified(
                        userLoginDTO.Password,
                        userData?.get(UserAccount.UserPassword)
                    )) {
                    throw BadRequestException("Authentication Error.")
                }

                userId = userData.get(UserAccount.UserName)
            }

            if (userId == null) throw BadRequestException("Authentication Error.")

            call.sessions.set("login_data", UserIdAuthorityPrincipal(userId.toString()))
            call.respond(mapOf("OK" to true))
        }

        post("/logout") {
            call.sessions.clear("login_data")
            call.respond(mapOf("OK" to true))
        }

        post ("/create") {
            val UserInformation = call.receive<User>()
            try {
                transaction {
                    UserAccount.insert {
                        it[UserName] = UserInformation.Name
                        it[UserPassword] = PasswordHasher.hashPassword(UserInformation.Password)
                    }
                }
            }catch (error: Exception){
                call.respond(mapOf(
                    "Fail" to true
                ))
            }

            call.respond(mapOf(
                "OK" to true
            ))
        }
        route("/"){
            authenticate("Session Auth"){
                get {

                }
            }
            get { call.respond("Nothing" to true) }

        }
    }
}


