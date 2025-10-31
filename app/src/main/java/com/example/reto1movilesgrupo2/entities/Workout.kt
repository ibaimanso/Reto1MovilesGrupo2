package com.example.reto1movilesgrupo2.entities

data class Workout (
    var id: Int = 0,
    var level: Int = 0,
    var name: String? = null,
    var descript: String? = null,
    var videoUrl: String? = null
)