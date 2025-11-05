package com.example.reto1movilesgrupo2.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.controllers.ControllerFactory
import com.example.reto1movilesgrupo2.entities.Exercise
import com.example.reto1movilesgrupo2.entities.User
import com.example.reto1movilesgrupo2.entities.UserWorkoutLine
import com.example.reto1movilesgrupo2.entities.Workout
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HistorialActivity : AppCompatActivity() {

    private lateinit var btnVolver: Button
    private lateinit var historialTable: TableLayout
    private lateinit var appLogoHistorial: ImageView
    private lateinit var userName: String
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appLogoHistorial = findViewById(R.id.appLogoHistorial)
        appLogoHistorial.setImageResource(R.mipmap.iconogym)

        btnVolver = findViewById(R.id.btnVolver)
        historialTable = findViewById(R.id.historialTable)

        userName = intent.getStringExtra("USERFNAME") ?: ""

        lifecycleScope.launch {
            getUserId()
            loadHistorial()
        }

        btnVolver.setOnClickListener {
            goBack()
        }
    }

    private fun goBack() {
        val intent = Intent(this, WorkoutsActivity::class.java)
        intent.putExtra("USERFNAME", userName)
        startActivity(intent)
        finish()
    }

    private suspend fun getUserId() {
        val tempUser = User()
        tempUser.fname = userName
        val user = ControllerFactory.getInstance()?.getUserController()?.selectByFname(tempUser)
        userId = user?.id ?: 0
    }

    private suspend fun loadHistorial() {
        if (userId == 0) {
            Toast.makeText(this, "Error al cargar el usuario", Toast.LENGTH_SHORT).show()
            return
        }

        val allUserWorkoutLines = ControllerFactory.getInstance()?.getUserWorkoutLineController()?.selectAll() ?: mutableListOf()

        val userWorkoutLines = allUserWorkoutLines.filter { it.userId == userId }.toMutableList()

        if (userWorkoutLines.isEmpty()) {
            Toast.makeText(this, "No tienes workouts completados en el historial", Toast.LENGTH_LONG).show()
            showEmptyMessage()
            return
        }

        val allWorkouts = ControllerFactory.getInstance()?.getWorkoutController()?.selectAll() ?: mutableListOf()
        val allExercises = ControllerFactory.getInstance()?.getExerciseController()?.selectAll() ?: mutableListOf()
        val allUserExerciseLines = ControllerFactory.getInstance()?.getUserExerciseLineController()?.selectAll() ?: mutableListOf()

        for (userWorkoutLine in userWorkoutLines) {
            val workout = allWorkouts.find { it.id == userWorkoutLine.workoutId }
            if (workout != null) {
                addHistorialRow(userWorkoutLine, workout, allExercises, allUserExerciseLines)
            }
        }
    }

    private fun showEmptyMessage() {
        val emptyRow = TableRow(this)
        emptyRow.setPadding(0, 16, 0, 16)

        val emptyMessage = TextView(this).apply {
            text = getString(R.string.no_historial)
            gravity = Gravity.CENTER
            setPadding(16, 24, 16, 24)
            textSize = 16f
            setTextColor(Color.GRAY)
        }

        emptyRow.addView(emptyMessage)
        historialTable.addView(emptyRow)
    }

    private suspend fun addHistorialRow(
        userWorkoutLine: UserWorkoutLine,
        workout: Workout,
        allExercises: MutableList<Exercise>,
        allUserExerciseLines: MutableList<com.example.reto1movilesgrupo2.entities.UserExerciseLine>
    ) {
        val row = TableRow(this)
        row.setPadding(0, 8, 0, 8)

        fun createCell(texto: String?): TextView {
            return TextView(this).apply {
                text = texto ?: ""
                gravity = Gravity.CENTER
                setPadding(16, 12, 16, 12)
                maxLines = 2
                ellipsize = TextUtils.TruncateAt.END
            }
        }

        val nameView = createCell(workout.name)

        val levelView = createCell(workout.level.toString())

        val totalTime = calculateTotalTime(userWorkoutLine.workoutId)
        val totalTimeView = createCell("$totalTime min")

        val expectedTime = ControllerFactory.getInstance()?.getWorkoutController()?.getExpectedTime(workout) ?: 0
        val expectedTimeView = createCell("$expectedTime min")

        val formattedDate = formatDate(userWorkoutLine.doneDate)
        val dateView = createCell(formattedDate)

        val completionPercentage = calculateCompletionPercentage(
            workout,
            allExercises,
            allUserExerciseLines
        )
        val completionView = createCell("$completionPercentage%")

        val videoView = createCell("Ver video").apply {
            setTextColor(Color.BLUE)
            setOnClickListener {
                openVideo(workout.videoUrl)
            }
        }

        row.addView(nameView)
        row.addView(levelView)
        row.addView(totalTimeView)
        row.addView(expectedTimeView)
        row.addView(dateView)
        row.addView(completionView)
        row.addView(videoView)

        historialTable.addView(row)
    }

    private suspend fun calculateTotalTime(workoutId: Int): Int {
        val workout = Workout(id = workoutId)
        val exercises = ControllerFactory.getInstance()?.getExerciseController()?.selectAllByWorkout(workout) ?: mutableListOf()

        var totalTime = 0
        for (exercise in exercises) {
            val exerciseTime = ControllerFactory.getInstance()?.getExerciseController()?.getExpectedTime(exercise) ?: 0
            totalTime += exerciseTime
        }

        return totalTime
    }

    private fun calculateCompletionPercentage(
        workout: Workout,
        allExercises: MutableList<Exercise>,
        allUserExerciseLines: MutableList<com.example.reto1movilesgrupo2.entities.UserExerciseLine>
    ): Int {
        val workoutExercises = allExercises.filter { it.workoutId == workout.id }

        if (workoutExercises.isEmpty()) {
            return 0
        }

        var completedExercises = 0
        for (exercise in workoutExercises) {
            val isCompleted = allUserExerciseLines.any {
                it.userId == userId && it.exerciseId == exercise.id
            }
            if (isCompleted) {
                completedExercises++
            }
        }

        return (completedExercises * 100) / workoutExercises.size
    }

    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) {
            return "Sin fecha"
        }

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            if (date != null) {
                outputFormat.format(date)
            } else {
                dateString.substring(0, 10)
            }
        } catch (_: Exception) {
            try {
                dateString.substring(0, 10).replace("-", "/")
            } catch (_: Exception) {
                "Fecha inválida"
            }
        }
    }

    private fun openVideo(videoUrl: String?) {
        if (videoUrl.isNullOrEmpty()) {
            Toast.makeText(this, "No hay video disponible", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(videoUrl))
            startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(this, "No se pudo abrir el video", Toast.LENGTH_LONG).show()
        }
    }
}
