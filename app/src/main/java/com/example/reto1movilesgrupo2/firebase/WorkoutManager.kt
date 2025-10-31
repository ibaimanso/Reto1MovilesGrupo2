package com.example.reto1movilesgrupo2.firebase

import com.example.reto1movilesgrupo2.entities.User
import com.example.reto1movilesgrupo2.entities.Workout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WorkoutManager {

    private val COLLECTION_NAME: String = "workouts"

    companion object {
        private var instance: WorkoutManager? = null

        fun getInstance(): WorkoutManager? {
            if (instance == null) {
                instance = WorkoutManager()
            }
            return instance
        }
    }

    suspend fun selectAll(): MutableList<Workout> {
        val ret = mutableListOf<Workout>()

        try {
            // Obtener todos los documentos de la colección "users"
            val workouts = ManagerFactory().getDB(COLLECTION_NAME).get().await()

            // Recorrer los documentos y convertirlos en objetos User
            for (document in workouts) {
                val workout = document.toObject(Workout::class.java)
                ret.add(Workout(
                    workout.id,
                    workout.level,
                    workout.name,
                    workout.descript,
                    workout.videoUrl
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
    }

    suspend fun insert(workout: Workout) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME)
                .document(workout.id.toString())
                .set(Workout(
                    workout.id,
                    workout.level,
                    workout.name,
                    workout.descript,
                    workout.videoUrl
                ))
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    suspend fun update(workout: Workout) {
        val workoutMap = hashMapOf<String, Any?>(
            "id" to workout.id,
            "level" to workout.level,
            "name" to workout.name,
            "descript" to workout.descript,
            "videoUrl" to workout.videoUrl
        )

        ManagerFactory().getDB(COLLECTION_NAME)
            .document(workout.id.toString())
            .update(workoutMap)
            .await()
    }

    suspend fun delete(workout: Workout) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME)
                .document(workout.id.toString())
                .delete()
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

}