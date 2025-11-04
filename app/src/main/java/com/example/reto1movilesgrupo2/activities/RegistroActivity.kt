package com.example.reto1movilesgrupo2.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.controllers.ControllerFactory
import com.example.reto1movilesgrupo2.entities.User
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class RegistroActivity : AppCompatActivity() {
    private lateinit var logo:        ImageView
    private lateinit var inputFname:  EditText
    private lateinit var inputLname:  EditText
    private lateinit var inputPw:     EditText
    private lateinit var inputEmail:  EditText
    private lateinit var inputBirth:  EditText
    private lateinit var inputType:   Spinner
    private lateinit var btnRegister: Button
    private lateinit var btnGoBack:   Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        logo = findViewById(R.id.appLogo)
        logo.setImageResource(R.mipmap.iconogym)

        inputFname = findViewById(R.id.inputName)
        inputLname = findViewById(R.id.inputLName)
        inputPw    = findViewById(R.id.inputPassword)
        inputEmail = findViewById(R.id.inputEmail)
        inputBirth = findViewById(R.id.inputBirth)

        val typeValues = arrayOf(
            "Ususario",
            "Entrenador"
        )
        val typeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            typeValues
        )
        typeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        inputType  = findViewById(R.id.userType)
        inputType.adapter = typeAdapter

        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            lifecycleScope.launch {
                register()
            }
        }

        btnGoBack = findViewById(R.id.btnBack)
        btnGoBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun register() {
        val user = User()

        user.fname = inputFname.text.toString()
        user.lname = inputLname.text.toString()
        user.pw    = inputPw.text.toString()
        user.email = inputEmail.text.toString()
        user.birth = inputBirth.text.toString()

        if (user.fname.equals("")) {
            Toast.makeText(
                this,
                "El nombre está vacío.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (user.pw.equals("")) {
            Toast.makeText(
                this,
                "La contraseña está vacía.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (ControllerFactory().getUserController().existUser(user)) {
            Toast.makeText(
                this,
                "El nombre de usuario está en uso.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        user.lastMod = LocalDateTime.now().toString()
        user.trainer = inputType.selectedItem.toString() == "Entrenador"

        ControllerFactory().getUserController().insert(user)

        Toast.makeText(
            this,
            "Registrado con exito.",
            Toast.LENGTH_LONG
        ).show()


        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}