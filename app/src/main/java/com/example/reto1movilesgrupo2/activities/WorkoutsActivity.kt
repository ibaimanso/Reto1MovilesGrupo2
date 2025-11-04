package com.example.reto1movilesgrupo2.activities

import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.controllers.ControllerFactory
import com.example.reto1movilesgrupo2.entities.User
import com.example.reto1movilesgrupo2.entities.Workout
import com.example.reto1movilesgrupo2.firebase.ManagerFactory
import kotlinx.coroutines.launch
import java.util.Locale

class WorkoutsActivity : AppCompatActivity() {

    private lateinit var btnProfile: Button
    private lateinit var btnBack: Button
    private lateinit var btnFilter: Button
    private lateinit var btnTrainer: Button

    private lateinit var workoutTable: TableLayout

    private lateinit var logo: ImageView

    private lateinit var userLevel: TextView

    private lateinit var inputLevelFilter: EditText

    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workouts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        logo = findViewById(R.id.appLogo)
        logo.setImageResource(R.mipmap.iconogym)

        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)
        btnFilter = findViewById(R.id.btnFilter)
        btnTrainer = findViewById(R.id.btnTrainer)

        workoutTable = findViewById(R.id.workoutTable)

        userName = intent.getStringExtra("USERFNAME").toString()

        btnTrainer.visibility = Button.GONE

        lifecycleScope.launch {
            val workoutsByLevel: MutableList<Workout>? = loadWorkoutsByLevel()
            insertIntoTable(workoutsByLevel)

            showTrainerButtonIfNeeded()
        }


        btnBack.setOnClickListener {
            goBack()
        }

        btnTrainer.setOnClickListener {
            lifecycleScope.launch {
                goToTrainer()
            }
        }

        btnProfile.setOnClickListener {
            goToProfile()
        }

    }

    private fun goBack() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToTrainer() {
        val intent = Intent(this, EntrenadorActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToProfile() {
        val intent = Intent(this, PerfilActivity::class.java)
        startActivity(intent)
        finish()
    }

    suspend fun loadWorkoutsByLevel(): MutableList<Workout>? {
        val tempUser = User()
        tempUser.fname = userName

        var user: User? =
            ControllerFactory.getInstance()?.getUserController()?.selectByFname(tempUser)
        if (user == null) user = User()

        val workouts: MutableList<Workout>? =
            ControllerFactory.getInstance()?.getWorkoutController()?.selectByLevel(user)

        return workouts

    }

    suspend fun showTrainerButtonIfNeeded() {
        val tempUser = User()
        tempUser.fname = userName

        var user: User? =
            ControllerFactory.getInstance()?.getUserController()?.selectByFname(tempUser)
        if (user == null) user = User()

        if (user.trainer) {
            btnTrainer.visibility = Button.VISIBLE
        }
    }

    suspend fun insertIntoTable(workouts: MutableList<Workout>?) {
        val dateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())

        if (workouts == null) {
            return
        }

        for (workout: Workout in workouts) {
            val row = TableRow(this)

            fun createCell(texto: String?): TextView {
                return TextView(this).apply {
                    text = texto
                    gravity = Gravity.CENTER
                    setPadding(16, 12, 16, 12)
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                }
            }

            // Crear las celdas
            val nameView = createCell(workout.name)
            val levelView = createCell(workout.level.toString())
            val timeView = createCell("${ControllerFactory().getWorkoutController().getExpectedTime(workout)} min")
            val dateView = createCell("")
            val completedView = createCell("")
            val videoView = createCell(workout.videoUrl)

            // Añadir las celdas a la fila
            row.addView(nameView)
            row.addView(levelView)
            row.addView(timeView)
            row.addView(dateView)
            row.addView(completedView)
            row.addView(videoView)

            // Agregar la fila a la tabla
            workoutTable.addView(row)
        }
    }
}