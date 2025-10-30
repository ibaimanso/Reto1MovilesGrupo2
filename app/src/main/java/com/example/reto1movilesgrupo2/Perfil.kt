package com.example.reto1movilesgrupo2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.LocaleListCompat
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

    private lateinit var spinnerLanguage: Spinner
    private lateinit var switchTheme: SwitchCompat

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
        btnGuardar = findViewById(R.id.btnGuardar)
        btnAtras = findViewById(R.id.btnAtras)
        spinnerLanguage = findViewById(R.id.spinnerLanguage)
        switchTheme = findViewById(R.id.switchTheme)

        firestore = FirebaseFirestore.getInstance()

        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        switchTheme.isChecked = currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

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

        initLanguageSpinner()
    }

    private fun initLanguageSpinner() {
        val languages = arrayOf(getString(R.string.language_spanish), getString(R.string.language_english))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter

        spinnerLanguage.setSelection(0)

        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val languageCode = when (position) {
                    0 -> "es"
                    1 -> "en"
                    else -> "es"
                }
                val currentLocale = AppCompatDelegate.getApplicationLocales()[0]?.language
                if (currentLocale != languageCode) {
                    setLocale(languageCode)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No es necesario hacer nada aquí
            }
        }
    }

    private fun setLocale(languageCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
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
                    inputApellidos.setText(doc.getString("lname ") ?: "")
                    inputEmail.setText(doc.getString("email") ?: "")
                    inputFecha.setText(doc.getString("birth") ?: "")


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