package com.example.reto1movilesgrupo2

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.format
import kotlin.text.get
import kotlin.text.matches
import kotlin.text.set
import kotlin.toString

class Registro : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var inputUsuario: EditText
    private lateinit var inputClave: EditText
    private lateinit var inputNombre: EditText
    private lateinit var inputApellidos: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputFecha: EditText
    private lateinit var spinner: Spinner
    private lateinit var btnRegistro: Button
    private lateinit var btnVolver: Button

    private var esEntrenador: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            FirebaseApp.initializeApp(this)
            db = FirebaseFirestore.getInstance()

            setContentView(R.layout.activity_registro)

            inputUsuario = findViewById(R.id.inputUsuarioRegistro)
            inputClave = findViewById(R.id.inputClaveRegistro)
            inputNombre = findViewById(R.id.inputNombreRegistro)
            inputApellidos = findViewById(R.id.inputApellidosRegistro)
            inputEmail = findViewById(R.id.inputEmailRegistro)
            inputFecha = findViewById(R.id.inputFechaRegistro)
            spinner = findViewById(R.id.spinner)
            btnRegistro = findViewById(R.id.registroButton2)
            btnVolver = findViewById(R.id.registroButton3)

            inputClave.transformationMethod = PasswordTransformationMethod.getInstance()

            configurarSpinner()

            btnRegistro.setOnClickListener {
                registrarUsuario()
            }

            btnVolver.setOnClickListener {
                finish()
            }
        } catch (e: Exception) {
            Log.e("Registro", "Error al inicializar la actividad: ${e.message}")
            Toast.makeText(this, "Error al inicializar: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun configurarSpinner() {
        val opciones = arrayOf("Usuario", "Entrenador")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                esEntrenador = position == 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                esEntrenador = false
            }
        }
    }

    private fun registrarUsuario() {
        val pw = inputClave.text.toString().trim()
        val name = inputNombre.text.toString().trim()
        val lname = inputApellidos.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val fechaNacimiento = inputFecha.text.toString().trim()

        if (pw.isEmpty() || name.isEmpty() || lname.isEmpty() ||
            email.isEmpty() || fechaNacimiento.isEmpty()
        ) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Formato de email inválido", Toast.LENGTH_SHORT).show()
            return
        }

        if (!validarFormatoFecha(fechaNacimiento)) {
            Toast.makeText(this, "Formato de fecha inválido (AAAA-MM-DD)", Toast.LENGTH_SHORT)
                .show()
            return
        }

        db.collection("users")
            .whereEqualTo("fname", name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    Toast.makeText(
                        this,
                        "Este nombre ya está en uso, prueba con otro!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    obtenerSiguienteId { nuevoId ->
                        guardarUsuario(nuevoId, pw, name, lname, email, fechaNacimiento)
                    }
                    Toast.makeText(this, "Registro realizado con éxito", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun obtenerSiguienteId(callback: (Int) -> Unit) {
        db.collection("users").get()
            .addOnSuccessListener { querySnapshot ->
                val nuevoId = querySnapshot.size() + 1
                callback(nuevoId)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al obtener ID: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun guardarUsuario(
        id: Int, pw: String, name: String, lname: String,
        email: String, birth: String
    ) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        val lastModStr = sdf.format(Date())

        val datosUsuario = hashMapOf(
            "id" to id,
            "pw" to pw,
            "fname" to name,
            "lname" to lname,
            "email" to email,
            "birth" to birth,
            "level" to 0,
            "trainer" to esEntrenador,
            "lastMod" to lastModStr
        )

        db.collection("users").document(id.toString()).set(datosUsuario)
            .addOnSuccessListener {
                val resultIntent = Intent().apply {
                    putExtra("registro_exitoso", true)
                    putExtra("mensaje_registro", "Usuario registrado correctamente!")
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al registrar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validarFormatoFecha(fecha: String): Boolean {

        val regex = Regex("""\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[12]\d|3[01])""")
        if (!regex.matches(fecha)) return false

        return try {
            val formato = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
            formato.isLenient = false
            formato.parse(fecha)
            true
        } catch (e: Exception) {
            false
        }

    }

    private fun nombreExiste(fname: String): Boolean {
        var ret = false
        val name = inputNombre.text.toString().trim()

        db.collection("users")
            .whereEqualTo("fname", fname)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (name.equals(querySnapshot)) {
                    ret = true
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al verificar nombre: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        return ret
    }

}