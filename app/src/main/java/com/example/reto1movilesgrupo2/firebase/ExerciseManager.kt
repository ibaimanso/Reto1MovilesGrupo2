package com.example.reto1movilesgrupo2.firebase

import com.example.reto1movilesgrupo2.entities.Exercise
import com.example.reto1movilesgrupo2.firebase.ManagerFactory
import kotlinx.coroutines.tasks.await

class ExerciseManager {

    private val COLLECTION_NAME: String = "exercises"

    companion object {
        private var instance: ExerciseManager? = null

        fun getInstance(): ExerciseManager? {
            if (instance == null) {
                instance = ExerciseManager()
            }
            return instance
        }
    }

    suspend fun selectAll(): MutableList<Exercise> {
        val ret = mutableListOf<Exercise>()

        try {
            // Obtener todos los documentos de la colección "Exercises"
            val exercises = ManagerFactory().getDB(COLLECTION_NAME).get().await()

            // Recorrer los documentos y convertirlos en objetos Exercise
            for (document in exercises) {
                val exercise = document.toObject(Exercise::class.java)
                ret.add(Exercise(
                    exercise.id,
                    exercise.workoutId,
                    exercise.name,
                    exercise.descript
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
    }

    suspend fun insert(exercise: Exercise) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME).document(exercise.id.toString())
                .set(Exercise(
                    exercise.id,
                    exercise.workoutId,
                    exercise.name,
                    exercise.descript
                ))
                .await()

        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun update(exercise: Exercise) {
        val exerciseMap = hashMapOf<String, Any?>(
            "id" to exercise.id,
            "workoutId" to exercise.workoutId,
            "name" to exercise.name,
            "descript" to exercise.descript
        )

        ManagerFactory().getDB(COLLECTION_NAME).document(exercise.id.toString())
            .update(exerciseMap)
            .await()
    }

    suspend fun delete(exercise: Exercise) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME).document(exercise.id.toString())
                .delete()
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

}