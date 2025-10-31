package com.example.reto1movilesgrupo2.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.entities.Exercise
import com.example.reto1movilesgrupo2.entities.Workout
import com.example.reto1movilesgrupo2.firebase.ManagerFactory

class ExerciseController : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public fun selectAll(): MutableList<Exercise> {
        return ManagerFactory().getExerciseManager().selectAll()
    }

    public fun insert(newExercise: Exercise) {
        ManagerFactory().getExerciseManager().insert(newExercise)
    }

    public fun update(exerciseToUpdate: Exercise) {
        ManagerFactory().getExerciseManager().update(exerciseToUpdate)
    }

    public fun delete(exerciseToDelete: Exercise) {
        ManagerFactory().getExerciseManager().delete(exerciseToDelete)
    }
}