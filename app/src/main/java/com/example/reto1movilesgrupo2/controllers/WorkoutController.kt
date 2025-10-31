package com.example.reto1movilesgrupo2.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.entities.User
import com.example.reto1movilesgrupo2.entities.Workout
import com.example.reto1movilesgrupo2.firebase.ManagerFactory

class WorkoutController : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    suspend fun selectAll(): MutableList<Workout> {
        return ManagerFactory().getWorkoutManager().selectAll()
    }

    suspend fun insert(newWorkout: Workout) {
        ManagerFactory().getWorkoutManager().insert(newWorkout)
    }

    suspend fun update(workoutToUpdate: Workout) {
        ManagerFactory().getWorkoutManager().update(workoutToUpdate)
    }

    suspend fun delete(workoutToDelete: Workout) {
        ManagerFactory().getWorkoutManager().delete(workoutToDelete)
    }
}