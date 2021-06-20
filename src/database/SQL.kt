package com.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table

object UserAccount : Table() {
    val UserName = text("UserName").primaryKey()
    val UserPassword = text("UserPassword")
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
}