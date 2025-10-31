package com.example.reto1movilesgrupo2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.entities.User
import kotlinx.coroutines.launch

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
        inputType  = findViewById(R.id.userType)

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

    private fun register() {
        val user = User()

        user.fname = inputFname.text.toString()
        user.lname = inputLname.text.toString()
        user.pw    = inputPw.text.toString()
        user.email = inputEmail.text.toString()
        user.birth = inputBirth.text.toString()

        // #TODO
    }
}