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
import com.example.database.SQL.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class Snippet(val text: String)

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
    val user = environment.config.property("ktor.database.user").getString()
    val password = environment.config.property("ktor.database.password").getString()
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = user, password = password)
    routing {
        post ("/login"){
            val userinput = call.receive<User>()
            if (userinput.Name.length < 6 || userinput.Password.length < 8) {

            }
        }
    }
}
