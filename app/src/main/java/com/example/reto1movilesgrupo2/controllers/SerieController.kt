package com.example.reto1movilesgrupo2.controllers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.entities.Exercise
import com.example.reto1movilesgrupo2.entities.Serie
import com.example.reto1movilesgrupo2.firebase.ManagerFactory

class SerieController : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    suspend fun selectAll(): MutableList<Serie> {
        return ManagerFactory().getSerieManager().selectAll()
    }

    suspend fun insert(newSerie: Serie) {
        ManagerFactory().getSerieManager().insert(newSerie)
    }

    suspend fun update(serieToUpdate: Serie) {
        ManagerFactory().getSerieManager().update(serieToUpdate)
    }

    suspend fun delete(serieToDelete: Serie) {
        ManagerFactory().getSerieManager().delete(serieToDelete)
    }
}