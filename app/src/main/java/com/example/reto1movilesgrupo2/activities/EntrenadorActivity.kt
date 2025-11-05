package com.example.reto1movilesgrupo2.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.reto1movilesgrupo2.R
import com.example.reto1movilesgrupo2.controllers.ControllerFactory
import com.example.reto1movilesgrupo2.entities.Exercise
import com.example.reto1movilesgrupo2.entities.Workout
import kotlinx.coroutines.launch

class EntrenadorActivity : AppCompatActivity() {

    private lateinit var btnVolver: Button
    private lateinit var inputFilter: EditText
    private lateinit var btnFilter: Button
    private lateinit var workoutTable: TableLayout
    private lateinit var btnAdd: Button
    private lateinit var btnModify: Button
    private lateinit var btnDelete: Button
    private lateinit var appLogo2: ImageView

    private var allWorkouts: MutableList<Workout> = mutableListOf()
    private var selectedWorkout: Workout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entrenador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appLogo2 = findViewById(R.id.appLogo2)
        appLogo2.setImageResource(R.mipmap.iconogym)

        btnVolver = findViewById(R.id.btnVolver)
        inputFilter = findViewById(R.id.inputFilter)
        btnFilter = findViewById(R.id.btnFilter)
        workoutTable = findViewById(R.id.workoutTable)
        btnAdd = findViewById(R.id.btnAdd)
        btnModify = findViewById(R.id.btnModify)
        btnDelete = findViewById(R.id.btnDelete)

        lifecycleScope.launch {
            loadWorkouts()
        }

        btnVolver.setOnClickListener {
            goBack()
        }

        btnFilter.setOnClickListener {
            lifecycleScope.launch {
                filterWorkoutsByLevel()
            }
        }

        btnAdd.setOnClickListener {
            showAddWorkoutDialog()
        }

        btnModify.setOnClickListener {
            if (selectedWorkout != null) {
                showModifyWorkoutDialog(selectedWorkout!!)
            } else {
                Toast.makeText(this, "Selecciona un workout de la tabla", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            if (selectedWorkout != null) {
                showDeleteConfirmationDialog(selectedWorkout!!)
            } else {
                Toast.makeText(this, "Selecciona un workout de la tabla", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goBack() {
        val intent = Intent(this, WorkoutsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private suspend fun loadWorkouts() {
        allWorkouts = ControllerFactory.getInstance()?.getWorkoutController()?.selectAll() ?: mutableListOf()

        if (allWorkouts.isEmpty()) {
            Toast.makeText(this, "No hay workouts disponibles en el sistema", Toast.LENGTH_LONG).show()
        }

        refreshTable(allWorkouts)
    }

    private suspend fun filterWorkoutsByLevel() {
        val levelText = inputFilter.text.toString().trim()

        if (levelText.isEmpty()) {
            refreshTable(allWorkouts)
            Toast.makeText(this, "Mostrando todos los workouts", Toast.LENGTH_SHORT).show()
            return
        }

        val level = levelText.toIntOrNull()
        if (level == null) {
            Toast.makeText(this, "Por favor, introduce un nivel válido (número)", Toast.LENGTH_SHORT).show()
            return
        }

        val filtered = allWorkouts.filter { workout ->
            workout.level == level
        }.toMutableList()

        if (filtered.isEmpty()) {
            Toast.makeText(this, "No hay workouts para el nivel $level", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Mostrando ${filtered.size} workout(s) de nivel $level", Toast.LENGTH_SHORT).show()
        }

        refreshTable(filtered)
    }

    private suspend fun refreshTable(workouts: MutableList<Workout>) {
        workoutTable.removeViews(1, workoutTable.childCount - 1)

        if (workouts.isEmpty()) {
            val emptyRow = TableRow(this)
            emptyRow.setPadding(0, 16, 0, 16)

            val emptyMessage = TextView(this).apply {
                text = "No hay workouts disponibles"
                gravity = Gravity.CENTER
                setPadding(16, 24, 16, 24)
                textSize = 16f
                setTextColor(Color.GRAY)
            }

            emptyRow.addView(emptyMessage)
            workoutTable.addView(emptyRow)
            return
        }

        for (workout in workouts) {
            val row = TableRow(this)
            row.setPadding(0, 8, 0, 8)

            fun createCell(texto: String?): TextView {
                return TextView(this).apply {
                    text = texto ?: ""
                    gravity = Gravity.CENTER
                    setPadding(16, 12, 16, 12)
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                }
            }

            val nameView = createCell(workout.name)
            val descriptView = createCell(workout.descript)
            val levelView = createCell(workout.level.toString())

            val exercisesView = createCell("Ver ejercicios").apply {
                setTextColor(Color.rgb(0, 102, 204))
                setOnClickListener {
                    lifecycleScope.launch {
                        showExercises(workout)
                    }
                }
            }

            val videoView = createCell(workout.videoUrl).apply {
                setTextColor(Color.BLUE)
                setOnClickListener {
                    openVideo(workout.videoUrl)
                }
            }

            row.addView(nameView)
            row.addView(descriptView)
            row.addView(levelView)
            row.addView(exercisesView)
            row.addView(videoView)

            row.setOnClickListener {
                selectedWorkout = workout
                highlightSelectedRow(row)
                Toast.makeText(this, "Seleccionado: ${workout.name}", Toast.LENGTH_SHORT).show()
            }

            workoutTable.addView(row)
        }
    }

    private suspend fun showExercises(workout: Workout) {
        val exercises = ControllerFactory.getInstance()?.getExerciseController()?.selectAllByWorkout(workout) ?: mutableListOf()

        if (exercises.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Ejercicios de ${workout.name}")
                .setMessage("Este workout no tiene ejercicios asignados aún.")
                .setPositiveButton("Aceptar", null)
                .show()
            return
        }

        val message = StringBuilder()
        for ((index, exercise) in exercises.withIndex()) {
            message.append("${index + 1}. ${exercise.name}\n")
            message.append("   ${exercise.descript ?: "Sin descripción"}\n")

            val series = ControllerFactory.getInstance()?.getSerieController()?.selectAllByExercise(exercise) ?: mutableListOf()
            message.append("   Series: ${series.size}\n")

            if (index < exercises.size - 1) {
                message.append("\n")
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Ejercicios de ${workout.name}")
            .setMessage(message.toString())
            .setPositiveButton("Aceptar", null)
            .show()
    }

    private fun highlightSelectedRow(selectedRow: TableRow) {
        for (i in 1 until workoutTable.childCount) {
            val row = workoutTable.getChildAt(i) as? TableRow
            row?.setBackgroundColor(Color.TRANSPARENT)
        }
        selectedRow.setBackgroundColor(Color.LTGRAY)
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

    private fun showAddWorkoutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_workout, null)
        val etName = dialogView.findViewById<EditText>(R.id.etWorkoutName)
        val etDescription = dialogView.findViewById<EditText>(R.id.etWorkoutDescription)
        val etLevel = dialogView.findViewById<EditText>(R.id.etWorkoutLevel)
        val etVideoUrl = dialogView.findViewById<EditText>(R.id.etWorkoutVideoUrl)

        AlertDialog.Builder(this)
            .setTitle("Añadir Workout")
            .setView(dialogView)
            .setPositiveButton("Añadir") { dialog, _ ->
                val name = etName.text.toString().trim()
                val description = etDescription.text.toString().trim()
                val levelStr = etLevel.text.toString().trim()
                val videoUrl = etVideoUrl.text.toString().trim()

                if (name.isEmpty() || levelStr.isEmpty()) {
                    Toast.makeText(this, "El nombre y nivel son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val level = levelStr.toIntOrNull()
                if (level == null) {
                    Toast.makeText(this, "El nivel debe ser un número", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    addWorkout(name, description, level, videoUrl)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private suspend fun addWorkout(name: String, description: String, level: Int, videoUrl: String) {
        val newId = (allWorkouts.maxOfOrNull { it.id } ?: 0) + 1
        val newWorkout = Workout(newId, level, name, description, videoUrl)

        try {
            ControllerFactory.getInstance()?.getWorkoutController()?.insert(newWorkout)
            Toast.makeText(this, "Workout añadido correctamente", Toast.LENGTH_SHORT).show()
            loadWorkouts()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al añadir workout: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showModifyWorkoutDialog(workout: Workout) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_workout, null)
        val etName = dialogView.findViewById<EditText>(R.id.etWorkoutName)
        val etDescription = dialogView.findViewById<EditText>(R.id.etWorkoutDescription)
        val etLevel = dialogView.findViewById<EditText>(R.id.etWorkoutLevel)
        val etVideoUrl = dialogView.findViewById<EditText>(R.id.etWorkoutVideoUrl)

        etName.setText(workout.name)
        etDescription.setText(workout.descript)
        etLevel.setText(workout.level.toString())
        etVideoUrl.setText(workout.videoUrl)

        AlertDialog.Builder(this)
            .setTitle("Modificar Workout")
            .setView(dialogView)
            .setPositiveButton("Guardar") { dialog, _ ->
                val name = etName.text.toString().trim()
                val description = etDescription.text.toString().trim()
                val levelStr = etLevel.text.toString().trim()
                val videoUrl = etVideoUrl.text.toString().trim()

                if (name.isEmpty() || levelStr.isEmpty()) {
                    Toast.makeText(this, "El nombre y nivel son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val level = levelStr.toIntOrNull()
                if (level == null) {
                    Toast.makeText(this, "El nivel debe ser un número", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    modifyWorkout(workout.id, name, description, level, videoUrl)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private suspend fun modifyWorkout(id: Int, name: String, description: String, level: Int, videoUrl: String) {
        val updatedWorkout = Workout(id, level, name, description, videoUrl)

        try {
            ControllerFactory.getInstance()?.getWorkoutController()?.update(updatedWorkout)
            Toast.makeText(this, "Workout modificado correctamente", Toast.LENGTH_SHORT).show()
            selectedWorkout = null
            loadWorkouts()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al modificar workout: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showDeleteConfirmationDialog(workout: Workout) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Workout")
            .setMessage("¿Estás seguro de que quieres eliminar '${workout.name}'?\n\nEsto también eliminará todos sus ejercicios y series asociadas.")
            .setPositiveButton("Eliminar") { dialog, _ ->
                lifecycleScope.launch {
                    deleteWorkout(workout)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private suspend fun deleteWorkout(workout: Workout) {
        try {
            val exercises = ControllerFactory.getInstance()?.getExerciseController()?.selectAllByWorkout(workout) ?: mutableListOf()

            for (exercise in exercises) {
                val series = ControllerFactory.getInstance()?.getSerieController()?.selectAllByExercise(exercise) ?: mutableListOf()
                for (serie in series) {
                    ControllerFactory.getInstance()?.getSerieController()?.delete(serie)
                }
                ControllerFactory.getInstance()?.getExerciseController()?.delete(exercise)
            }

            ControllerFactory.getInstance()?.getWorkoutController()?.delete(workout)
            Toast.makeText(this, "Workout eliminado correctamente", Toast.LENGTH_SHORT).show()
            selectedWorkout = null
            loadWorkouts()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al eliminar workout: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}