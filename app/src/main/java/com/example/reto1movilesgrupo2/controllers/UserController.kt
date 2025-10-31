package com.example.reto1movilesgrupo2.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.entities.User
import com.example.reto1movilesgrupo2.firebase.ManagerFactory

class UserController : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public fun selectAll(): MutableList<User> {
        //var users: MutableList<User> = mutableListOf()
        return ManagerFactory().getUserManager().selectAll()
    }

    public fun insert(newUser: User) {
        ManagerFactory().getUserManager().insert(newUser)
    }

    public fun update(userToUpdate: User) {
        ManagerFactory().getUserManager().update(userToUpdate)
    }

    public fun delete(userToDelete: User) {
        ManagerFactory().getUserManager().delete(userToDelete)
    }
}