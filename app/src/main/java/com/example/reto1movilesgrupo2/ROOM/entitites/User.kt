package com.example.reto1prueba.ROOM.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User (
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val level: Int = 0,
    val fname: String? = null,
    val lname: String? = null,
    val pw: String? = null,
    val email: String? = null,
    val birth: String? = null,
    val lastMod: String? = null,
    val trainer: Boolean = false
)