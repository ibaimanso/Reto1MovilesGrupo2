package com.example.reto1movilesgrupo2.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.controllers.ControllerFactory
import com.example.reto1movilesgrupo2.entities.User
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language
import java.time.LocalDateTime
import java.util.Locale

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

    @RequiresApi(Build.VERSION_CODES.O)
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
            lifecycleScope.launch {
                saveChanges()
            }
        }

        configSpinner()
        configSwitch()

    }

    private fun goBack() {
        val intent = Intent(this, WorkoutsActivity::class.java)
        intent.putExtra("USERFNAME", userName)
        startActivity(intent)
        finish()
    }

    private fun changeLanguage(languageCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    private fun configSpinner() {

        val languages = arrayOf(getString(R.string.language_spanish), getString(R.string.language_english))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        languageSpinner.setSelection(0)

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val languageCode = when (position) {
                    0 -> "es"
                    1 -> "en"
                    else -> "es"
                }
                val currentLocale = AppCompatDelegate.getApplicationLocales()[0]?.language

                if (currentLocale != languageCode) {
                    changeLanguage(languageCode)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No es necesario hacer nada aquí
            }
        }
    }

    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun configSwitch() {
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()

        switchTheme.isChecked = currentNightMode == AppCompatDelegate.MODE_NIGHT_YES

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setDarkMode()
            } else {
                setLightMode()
            }
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveChanges() {
        val userToSend = User()

        val tempUser = User()
        tempUser.fname = userName

        var user: User? =
            ControllerFactory().getUserController().selectByFname(tempUser)
        if (user == null) user = User()

        userToSend.id = user.id
        userToSend.lastMod = LocalDateTime.now().toString()
        userToSend.level = user.level
        userToSend.pw = user.pw
        userToSend.trainer = user.trainer

        userName = inputFName.text.toString()
        userToSend.fname = inputFName.text.toString()
        userToSend.lname = inputLName.text.toString()
        userToSend.email = inputEmail.text.toString()
        userToSend.birth = inputBirth.text.toString()

        ControllerFactory().getUserController().update(userToSend)

        Toast.makeText(this, "Cambios guardados.", Toast.LENGTH_LONG).show()
    }

}