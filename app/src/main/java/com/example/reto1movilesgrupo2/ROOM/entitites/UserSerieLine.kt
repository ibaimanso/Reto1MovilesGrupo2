package com.example.reto1prueba.ROOM.entitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userSerieLines")
class UserSerieLine (
    @PrimaryKey(autoGenerate = false) val userId: Int = 0,
    @PrimaryKey(autoGenerate = false) val serieId: Int = 0
)