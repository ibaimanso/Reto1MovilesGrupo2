package com.example.reto1movilesgrupo2.ROOM.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey
    val fname: String,
    val pw: String
)