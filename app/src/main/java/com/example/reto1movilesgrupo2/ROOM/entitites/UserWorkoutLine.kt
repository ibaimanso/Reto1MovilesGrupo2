package com.example.reto1movilesgrupo2.ROOM.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userWorkoutLines")
class UserWorkoutLine (
    @PrimaryKey(autoGenerate = false) val userId: Int = 0,
    @PrimaryKey(autoGenerate = false) val workoutId: Int = 0,
)