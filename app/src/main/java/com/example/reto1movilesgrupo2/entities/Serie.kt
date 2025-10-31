package com.example.reto1movilesgrupo2.entities

data class Serie (
    var id: Int = 0,
    var exerciseId: Int = 0,
    var expectedTime: Int = 0,
    var restTime: Int = 0,
    var repetitions: Int = 0,
    var name: String? = null,
    var iconPath: String? = null
)