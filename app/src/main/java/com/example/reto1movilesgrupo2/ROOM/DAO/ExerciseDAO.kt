package com.example.reto1prueba.ROOM.DAO

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.reto1prueba.ROOM.entitites.Exercise

interface ExerciseDAO {

    @Query("SELECT * FROM exercises ORDER BY id")
    fun getAll() :  List<Exercise>

    @Insert
    fun insertAll(vararg exercises: Exercise)

    @Delete
    fun delete(exercise: Exercise)

}