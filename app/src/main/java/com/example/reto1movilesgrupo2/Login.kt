package com.example.reto1movilesgrupo2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    private lateinit var inputUsuario: EditText
    private lateinit var inputClave: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegistro: Button
    private lateinit var checkRecordar: CheckBox

    private val registroLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val msg = result.data?.getStringExtra("mensaje_registro")
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputUsuario = findViewById(R.id.inputUsuarioLogin)
        inputClave = findViewById(R.id.inputClaveLogin)
        btnLogin = findViewById(R.id.loginButton)
        btnRegistro = findViewById(R.id.registroButton)
        checkRecordar = findViewById(R.id.checkBox)

        btnRegistro.setOnClickListener {
            try {
                val intent = Intent(this, Registro::class.java)
                registroLauncher.launch(intent)
            } catch (e: Exception) {
                Log.e("Login", "Error al abrir Registro: ${e.message}")
                Toast.makeText(this, "Error al abrir pantalla de registro: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        btnLogin.setOnClickListener {
        }
    }
}
