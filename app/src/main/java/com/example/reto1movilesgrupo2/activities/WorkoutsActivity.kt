package com.example.reto1movilesgrupo2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.reto1movilesgrupo2.R
import kotlinx.coroutines.launch

class WorkoutsActivity : AppCompatActivity() {

    private lateinit var btnProfile: Button
    private lateinit var btnBack:    Button
    private lateinit var btnFilter:  Button
    private lateinit var btnTrainer: Button

    private lateinit var logo: ImageView

    private lateinit var userLevel: TextView

    private lateinit var inputLevelFilter: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workouts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        logo = findViewById(R.id.appLogo)
        logo.setImageResource(R.mipmap.iconogym)

        btnProfile = findViewById(R.id.btnProfile)
        btnBack    = findViewById(R.id.btnBack)
        btnFilter  = findViewById(R.id.btnFilter)
        btnTrainer = findViewById(R.id.btnTrainer)

        val userName = intent.getStringExtra("USERFNAME")

        // Tenemos el usuario, hay que comprobar si es entrenador o no y mostrar o no el botón entrenador


        btnBack.setOnClickListener {
            goBack()
        }

        btnTrainer.setOnClickListener {
            goToTrainer()
        }
    }

    private fun goBack() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToTrainer() {
        val intent = Intent(this, EntrenadorActivity::class.java)
        startActivity(intent)
        finish()
    }



}