package com.example.database

object User: org.jetbrains.exposed.dao.id.LongIdTable(){
    var UserName = varchar("UserName", 255).uniqueIndex()
    var UserPassword = varchar("UserPassword", 255).uniqueIndex()
}

data class SQL(var UserName: String, var UserPassword: String) {

    fun CheckInside(account: String){

    }
}