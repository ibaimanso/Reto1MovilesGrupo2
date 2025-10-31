package com.example.reto1prueba.ROOM.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series")
data class Serie (
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    val exerciseId: Int = 0,
    val expectedTime: Int = 0,
    val restTime: Int = 0,
    val repetitions: Int = 0,
    val name: String? = null,
    val iconPath: String? = null
)