package com.example.reto1movilesgrupo2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.entities.User

class LoginActivity : AppCompatActivity() {

    private lateinit var inputUser: EditText
    private lateinit var inputPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputUser     = findViewById(R.id.inputUser)
        inputPassword = findViewById(R.id.inputPassword)
    }

    fun login() {
        val user = User()

        user.fname = inputUser.text.toString()
        user.pw    = inputPassword.text.toString()

        if (ControllerFacotry().getUserController().existsUser(user)) {
            val intent = Intent(this, WorkoutsActivity::class.java)
            intent.putExtra("USERFNAME", user.fname)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(
                this,
                "Las credenciales son incorrectas.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}