package com.example.reto1movilesgrupo2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Perfil : AppCompatActivity() {

    private lateinit var inputUsuario: EditText
    private lateinit var inputNombre: EditText
    private lateinit var inputApellidos: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputFecha: EditText
    private lateinit var spinnerGenero: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var btnAtras: Button

    private lateinit var firestore: FirebaseFirestore
    private var userId: String = ""
    private var documentId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        inputUsuario = findViewById(R.id.inputUsuarioRegistro2)
        inputNombre = findViewById(R.id.inputNombreRegistro2)
        inputApellidos = findViewById(R.id.inputApellidosRegistro2)
        inputEmail = findViewById(R.id.inputEmailRegistro2)
        inputFecha = findViewById(R.id.inputFechaRegistro2)
        spinnerGenero = findViewById(R.id.spinner2)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnAtras = findViewById(R.id.btnAtras)

        firestore = FirebaseFirestore.getInstance()

        val generos = arrayOf("Masculino", "Femenino", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adapter

        userId = intent.getStringExtra("USERNAME") ?: ""

        if (userId.isNotEmpty()) {
            cargarDatosUsuario()
        } else {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnAtras.setOnClickListener {
            finish()
        }

        btnGuardar.setOnClickListener {
            guardarCambios()
        }
    }

    private fun cargarDatosUsuario() {
        firestore.collection("users")
            .whereEqualTo("id", userId.toLongOrNull() ?: 0L)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val doc = querySnapshot.documents[0]
                    documentId = doc.id

                    inputUsuario.setText(doc.getString("fname") ?: "")
                    inputNombre.setText(doc.getString("fname") ?: "")
                    inputApellidos.setText(doc.getString("lname") ?: "")
                    inputEmail.setText(doc.getString("email") ?: "")
                    inputFecha.setText(doc.getString("birth") ?: "")

                    val genero = doc.getString("genero") ?: "Masculino"
                    val generos = arrayOf("Masculino", "Femenino", "Otro")
                    val position = generos.indexOf(genero)
                    if (position >= 0) {
                        spinnerGenero.setSelection(position)
                    }
                } else {
                    Toast.makeText(this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener { e ->
                Log.e("Perfil", "Error al cargar datos: ${e.message}")
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun guardarCambios() {
        if (documentId.isEmpty()) {
            Toast.makeText(this, "Error: No se puede guardar", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val datosActualizados = hashMapOf(
            "fname" to inputNombre.text.toString(),
            "apellidos" to inputApellidos.text.toString(),
            "email" to inputEmail.text.toString(),
            "birth" to inputFecha.text.toString(),
            "genero" to spinnerGenero.selectedItem.toString(),
            "lastMod" to currentDate
        )

        firestore.collection("users").document(documentId)
            .update(datosActualizados as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Perfil", "Error al guardar: ${e.message}")
                Toast.makeText(this, "Error al guardar cambios", Toast.LENGTH_SHORT).show()
            }
    }
}
