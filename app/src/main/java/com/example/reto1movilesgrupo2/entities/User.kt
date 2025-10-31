package com.example.reto1movilesgrupo2.entities

data class User (
    var trainer: Boolean = false,
    var id: Int = 0,
    var level: Int = 0,
    var fname: String? = null,
    var lname: String? = null,
    var pw: String? = null,
    var email: String? = null,
    var birth: String? = null,
    var lastMod: String? = null
)

