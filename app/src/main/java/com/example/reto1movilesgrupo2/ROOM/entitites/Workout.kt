package com.example.reto1prueba.ROOM.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
class Workout (
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val level: Int = 0,
    val name: String? = null,
    val descript: String? = null,
    val videoUrl: String? = null
)