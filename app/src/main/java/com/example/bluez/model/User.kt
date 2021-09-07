package com.example.bluez.model

data class User(
    val id: String,
    val email: String,
    val username: String,
    val phoneNumber: String,
    val lastName: String
){
    constructor():this("","","","","")
}