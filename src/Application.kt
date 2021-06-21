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
import com.example.database.*
import org.jetbrains.exposed.sql.Database.*
import org.jetbrains.exposed.sql.insert
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
    initDatabase()
    routing {
        post ("/login"){
            val userinput = call.receive<User>()
            if (userinput.Name.length < 6 || userinput.Password.length < 8) {

            }
        }
        post ("/create") {
            val UserInformation = call.receive<User>()
            transaction {
                    UserAccount.insert {
                        it[UserName] = UserInformation.Name
                        it[UserPassword] = UserInformation.Password
                    }
            }
            call.respond(mapOf(
                "OK" to true
            ))
        }
    }
}
