package com.example.reto1movilesgrupo2.activities

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.controllers.ControllerFactory
import com.example.reto1movilesgrupo2.entities.User
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language

class PerfilActivity : AppCompatActivity() {

    private lateinit var logo: ImageView

    private lateinit var btnBack: Button
    private lateinit var btnSave: Button

    private lateinit var inputFName: EditText
    private lateinit var inputLName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputBirth: EditText

    private lateinit var switchTheme: SwitchCompat

    private lateinit var languageSpinner: Spinner

    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        logo = findViewById(R.id.appLogo)
        logo.setImageResource(R.mipmap.iconogym)

        btnBack = findViewById(R.id.btnBack)
        btnSave = findViewById(R.id.btnSave)

        inputFName = findViewById(R.id.inputName)
        inputLName = findViewById(R.id.inputLName)
        inputEmail = findViewById(R.id.inputEmail)
        inputBirth = findViewById(R.id.inputBirth)

        switchTheme = findViewById(R.id.switchTheme)

        languageSpinner = findViewById(R.id.spinnerLanguage)

        userName = intent.getStringExtra("USERFNAME").toString()

        lifecycleScope.launch {
            loadData()
        }

        btnBack.setOnClickListener {
            goBack()
        }

        btnSave.setOnClickListener {

        }

    }

    private fun goBack() {
        val intent = Intent(this, WorkoutsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveChanges() {

    }

    suspend fun loadData() {
        val tempUser = User()
        tempUser.fname = userName

        var user: User? =
            ControllerFactory.getInstance()?.getUserController()?.selectByFname(tempUser)
        if (user == null) user = User()

        inputFName.setText(user.fname)
        inputLName.setText(user.lname)
        inputEmail.setText(user.email)
        inputBirth.setText(user.birth)
    }

}