package com.example.reto1movilesgrupo2.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.controllers.ControllerFactory
import com.example.reto1movilesgrupo2.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var inputUser: EditText
    private lateinit var inputPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var checkBox: CheckBox
    private lateinit var logo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        logo = findViewById(R.id.appLogo)
        logo.setImageResource(R.mipmap.iconogym)

        inputUser     = findViewById(R.id.inputUser)
        inputPassword = findViewById(R.id.inputPassword)
        btnLogin      = findViewById(R.id.btnLogin)
        btnRegister   = findViewById(R.id.btnRegister)
        checkBox      = findViewById(R.id.checkBox)

        btnLogin.setOnClickListener {
            lifecycleScope.launch {
                login()
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    suspend fun login() {
        val user = User()

        user.fname = inputUser.text.toString()
        user.pw    = inputPassword.text.toString()

        if (!ControllerFactory().getUserController().existUser(user)) {
            Toast.makeText(
                this,
                "El usuario no existe.",
                Toast.LENGTH_LONG
            ).show()
        } else if (!ControllerFactory().getUserController().existLogin(user)) {
            Toast.makeText(
                this,
                "Las credenciales son incorrectas.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val intent = Intent(this, WorkoutsActivity::class.java)
            intent.putExtra("USERFNAME", user.fname)
            startActivity(intent)
            finish()
        }
    }
}