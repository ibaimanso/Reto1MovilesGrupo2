package com.example.reto1movilesgrupo2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

data class Workout(
    val nombre: String,
    val nivel: Int,
    val tiempoTotal: Int,
    val fecha: String,
    val porcentajeCompletado: Int,
    val videoUrl: String
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

        val tableLayout = findViewById<TableLayout>(R.id.workoutTable)
        val workouts = listOf(
            Workout("Prueba", 0, 90, "2025-10-30", 50, "patata"),
            Workout("AGSGSG", 0, 90, "2025-10-30", 50, "patata"),
            Workout("PAPTAPTPA", 0, 90, "2025-10-30", 50, "patata"),
        )

        val dateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())

        for ((index, workout) in workouts.withIndex()) {
            val row = TableRow(this)

            // Alternar color de fondo por filas
            if (index % 2 == 0) {
                row.setBackgroundColor(Color.parseColor("#F8F8F8"))
            } else {
                row.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }

            // Crear una función auxiliar para hacer celdas con estilo
            fun crearCelda(texto: String): TextView {
                return TextView(this).apply {
                    text = texto
                    gravity = Gravity.CENTER
                    setPadding(16, 12, 16, 12)
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                }
            }

            // Crear las celdas
            val nombreView = crearCelda(workout.nombre)
            val nivelView = crearCelda(workout.nivel.toString())
            val tiempoView = crearCelda("${workout.tiempoTotal} min")
            val fechaView = crearCelda(workout.fecha)
            val completadoView = crearCelda("${workout.porcentajeCompletado}%")
            val videoView = crearCelda(workout.videoUrl)

            // Añadir las celdas a la fila
            row.addView(nombreView)
            row.addView(nivelView)
            row.addView(tiempoView)
            row.addView(fechaView)
            row.addView(completadoView)
            row.addView(videoView)

            // Agregar la fila a la tabla
            tableLayout.addView(row)
        }

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
        db.collection("workouts").get().addOnSuccessListener {

        }

    }

}
