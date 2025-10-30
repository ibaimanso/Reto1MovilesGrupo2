package com.example.reto1movilesgrupo2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

data class historyWorkout(
    val name: String,
    val level: String,
    val fecha: Long,
    val porcentajeCompletado: Int
)

class Workouts : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var btnPerfil: Button


    private var username: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts)

        btnPerfil = findViewById(R.id.btnPerfil)
        username = intent.getStringExtra("USERNAME") ?: ""


        if (username.isEmpty()) {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnPerfil.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }
    }

    private fun obtenerWorkouts() {

        db.collection("workouts").get().addOnSuccessListener {  }

    }

}
