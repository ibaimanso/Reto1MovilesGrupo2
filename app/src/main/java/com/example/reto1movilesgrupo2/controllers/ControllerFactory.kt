package com.example.reto1movilesgrupo2.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reto1movilesgrupo2.entities.Exercise
import com.example.reto1movilesgrupo2.controllers.ExerciseController
import com.example.reto1movilesgrupo2.controllers.SerieController
import com.example.reto1movilesgrupo2.controllers.UserExerciseLineController
import com.example.reto1movilesgrupo2.controllers.UserController
import com.example.reto1movilesgrupo2.controllers.UserSerieLineController
import com.example.reto1movilesgrupo2.controllers.UserWorkoutLineController
import com.example.reto1movilesgrupo2.controllers.WorkoutController

class ControllerFactory : AppCompatActivity() {

    companion object {
        private var instance: ControllerFactory? = null

        fun getInstance(): ControllerFactory? {
            if (instance == null) {
                instance = ControllerFactory()
            }
            return instance
        }
    }

    public fun getExerciseController(): ExerciseController {
        return ExerciseController()
    }

    public fun getSerieController(): SerieController {
        return SerieController()
    }

    public fun getUserExerciseLineController(): UserExerciseLineController {
        return UserExerciseLineController()
    }

    public fun getUserController(): UserController {
        return UserController()
    }

    public fun getUserSerieLineController(): UserSerieLineController {
        return UserSerieLineController()
    }

    public fun getUserWorkoutLineController(): UserWorkoutLineController {
        return UserWorkoutLineController()
    }

    public fun getWorkoutController(): WorkoutController {
        return WorkoutController()
    }
    
}