package com.example.reto1movilesgrupo2.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.entities.UserExerciseLine
import com.example.reto1movilesgrupo2.entities.UserWorkoutLine
import com.example.reto1movilesgrupo2.firebase.ManagerFactory

class UserWorkoutLineController : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    suspend fun selectAll(): MutableList<UserWorkoutLine> {
        return ManagerFactory().getUserWorkoutLineManager().selectAll()
    }

    suspend fun insert(newUserWorkoutLine: UserWorkoutLine) {
        return ManagerFactory().getUserWorkoutLineManager().insert(newUserWorkoutLine)
    }

    suspend fun update(userWorkoutLineToUpdate: UserWorkoutLine) {
        return ManagerFactory().getUserWorkoutLineManager().update(userWorkoutLineToUpdate)
    }

    suspend fun delete(userWorkoutLineToDelete: UserWorkoutLine) {
        return ManagerFactory().getUserWorkoutLineManager().delete(userWorkoutLineToDelete)
    }
}