package com.example.database

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.database.UserAccount.primaryKey
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object UserAccount : Table() {
    val UserName = text("UserName").primaryKey()
    val UserPassword = text("UserPassword")
    val authority = integer("Authority")
}

data class SQL(var UserName: String, var UserPassword: String) {

    fun CheckInside(account: String){

    }
}

fun initDatabase() {
    val config = HikariConfig("/hikari.properties")
    config.schema = "public"
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    transaction {
        SchemaUtils.create(UserAccount)
    }
}

const val BCRYPT_COST = 15

public object PasswordHasher {
    fun hashPassword(password: String) =
        BCrypt.withDefaults().hashToString(
            BCRYPT_COST,
            password.toCharArray()
        )

    fun verifyPassword(password: String, passwordHash: String) =
        BCrypt.verifyer().verify(
            password.toCharArray(),
            passwordHash
        )
}