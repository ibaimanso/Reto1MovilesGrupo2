package com.example.reto1prueba.ROOM.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise (
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    var workoutId: Int = 0,
    var name: String? = null,
    var descript: String? = null
)