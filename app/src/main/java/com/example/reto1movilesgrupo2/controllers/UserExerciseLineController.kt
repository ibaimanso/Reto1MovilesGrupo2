package com.example.reto1movilesgrupo2.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.entities.UserExerciseLine
import com.example.reto1movilesgrupo2.firebase.ManagerFactory

class UserExerciseLineController : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    suspend fun selectAll(): MutableList<UserExerciseLine> {
        return ManagerFactory().getUserExerciseLineManager().selectAll()
    }

    suspend fun insert(newUserExerciseLine: UserExerciseLine) {
        return ManagerFactory().getUserExerciseLineManager().insert(newUserExerciseLine)
    }

    suspend fun update(userExerciseLineToUpdate: UserExerciseLine) {
        return ManagerFactory().getUserExerciseLineManager().update(userExerciseLineToUpdate)
    }

    suspend fun delete(userExerciseLineToDelete: UserExerciseLine) {
        return ManagerFactory().getUserExerciseLineManager().delete(userExerciseLineToDelete)
    }

}