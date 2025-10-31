package com.example.reto1prueba.ROOM.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userExerciseLines")
class UserExerciseLine (
    @PrimaryKey(autoGenerate = false) val userId: Int = 0,
    @PrimaryKey(autoGenerate = false) val exerciseId: Int = 0
)