package com.example.reto1movilesgrupo2.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.entities.UserSerieLine
import com.example.reto1movilesgrupo2.entities.UserWorkoutLine
import com.example.reto1movilesgrupo2.firebase.ManagerFactory

class UserSerieLineController : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    suspend fun selectAll(): MutableList<UserSerieLine> {
        return ManagerFactory().getUserSerieLineManager().selectAll()
    }

    suspend fun insert(newUserSerieLine: UserSerieLine) {
        return ManagerFactory().getUserSerieLineManager().insert(newUserSerieLine)
    }

    suspend fun update(userSerieLineToUpdate: UserSerieLine) {
        return ManagerFactory().getUserSerieLineManager().update(userSerieLineToUpdate)
    }

    suspend fun delete(userSerieLineToDelete: UserSerieLine) {
        return ManagerFactory().getUserSerieLineManager().delete(userSerieLineToDelete)
    }
}