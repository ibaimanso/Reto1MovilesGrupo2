package com.example.reto1movilesgrupo2.entities

data class User (
    val trainer: Boolean = false,
    val id: Int = 0,
    val level: Int = 0,
    val fname: String? = null,
    val lname: String? = null,
    val pw: String? = null,
    val email: String? = null,
    val birth: String? = null,
    val lastMod: String? = null
)

