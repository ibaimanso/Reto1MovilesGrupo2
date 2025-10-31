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

    public fun selectAll(): MutableList<Workout> {
        return ManagerFactory().getWorkoutManager().selectAll()
    }

    public fun insert(newWorkout: Workout) {
        ManagerFactory().getWorkoutManager().insert(newWorkout)
    }

    public fun update(workoutToUpdate: Workout) {
        ManagerFactory().getWorkoutManager().update(workoutToUpdate)
    }

    public fun delete(workoutToDelete: Workout) {
        ManagerFactory().getWorkoutManager().delete(workoutToDelete)
    }
}