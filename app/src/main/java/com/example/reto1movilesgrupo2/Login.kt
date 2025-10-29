package com.example.reto1movilesgrupo2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private lateinit var inputUsuario: EditText
    private lateinit var inputClave: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegistro: Button
    private lateinit var checkRecordar: CheckBox

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputUsuario = findViewById(R.id.inputUsuarioLogin)
        inputClave = findViewById(R.id.inputClaveLogin)
        btnLogin = findViewById(R.id.loginButton)
        btnRegistro = findViewById(R.id.registroButton)
        checkRecordar = findViewById(R.id.checkBox)

        FirebaseApp.initializeApp(this)
        firestore = FirebaseFirestore.getInstance()

        cargarDatosGuardados()

        btnRegistro.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            realizarLogin()
        }
    }

    private fun cargarDatosGuardados() {
        val prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val savedUsername = prefs.getString("username", "") ?: ""
        val savedPassword = prefs.getString("password", "") ?: ""
        val remember = prefs.getBoolean("remember", false)

        if (remember && savedUsername.isNotEmpty()) {
            inputUsuario.setText(savedUsername)
            inputClave.setText(savedPassword)
            checkRecordar.isChecked = true
        }
    }

    private fun realizarLogin() {
        val userInput = inputUsuario.text.toString().trim()
        val passInput = inputClave.text.toString().trim()

        if (userInput.isEmpty() || passInput.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        btnLogin.isEnabled = false

        firestore.collection("users")
            .whereEqualTo("fname", userInput)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    btnLogin.isEnabled = true
                    Log.d("LoginDebug", "Encontrado(s) por lname='$userInput' count=${querySnapshot.size()}")
                    if (querySnapshot.size() > 1) {
                        Log.w("LoginDebug", "Hay ${querySnapshot.size()} documentos con el mismo lname='$userInput'. Se usará el primero.")
                    }
                    val doc = querySnapshot.documents[0]
                    procesarDocumentoLogin(doc, passInput)
                } else {
                    Log.d("LoginDebug", "No encontrado por lname exacto. Intentando lname_lower...")
                    val userLower = userInput.lowercase()
                    firestore.collection("users")
                        .whereEqualTo("lname_lower", userLower)
                        .get()
                        .addOnSuccessListener { qs2 ->
                            btnLogin.isEnabled = true
                            if (!qs2.isEmpty) {
                                Log.d("LoginDebug", "Encontrado por lname_lower='$userLower' count=${qs2.size()}")
                                val doc2 = qs2.documents[0]
                                procesarDocumentoLogin(doc2, passInput)
                            } else {
                                Log.d("LoginDebug", "No existe usuario con lname='$userInput' ni lname_lower='$userLower'")
                                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e2 ->
                            btnLogin.isEnabled = true
                            Log.e("Login", "Error en búsqueda lname_lower: ${e2.message}", e2)
                            Toast.makeText(this, "Error de conexión con Firebase", Toast.LENGTH_LONG).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                btnLogin.isEnabled = true
                Log.e("Login", "Error en query por lname: ${e.message}", e)
                Toast.makeText(this, "Error de conexión con Firebase", Toast.LENGTH_LONG).show()
            }
    }

    private fun procesarDocumentoLogin(doc: DocumentSnapshot, passInput: String) {
        Log.d("LoginDebug", "Doc encontrado id=${doc.id} data=${doc.data}")
        val storedPassword = doc.getString("pw") ?: ""
        if (storedPassword == passInput) {
            val userId = doc.getLong("id")?.toString() ?: doc.id
            guardarCredenciales(userId, passInput)
            navegarAWorkouts(userId)
        } else {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarCredenciales(username: String, password: String) {
        val prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString("username", username)
        editor.putBoolean("remember", checkRecordar.isChecked)

        if (checkRecordar.isChecked) {
            editor.putString("password", password)
        } else {
            editor.remove("password")
        }

        editor.apply()
    }

    private fun navegarAWorkouts(username: String) {
        Toast.makeText(this, "Acceso exitoso", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Workouts::class.java)
        intent.putExtra("USERNAME", username)
        startActivity(intent)
        finish()
    }
}
