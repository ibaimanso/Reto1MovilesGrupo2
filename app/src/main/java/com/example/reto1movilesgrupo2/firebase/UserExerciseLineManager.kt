package com.example.reto1movilesgrupo2.firebase

import com.example.reto1movilesgrupo2.entities.Exercise
import com.example.reto1movilesgrupo2.entities.UserExerciseLine
import com.example.reto1movilesgrupo2.entities.UserWorkoutLine
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserExerciseLineManager {

    private val COLLECTION_NAME: String = "userExerciseLines"

    companion object {
        private var instance: UserExerciseLineManager? = null

        fun getInstance(): UserExerciseLineManager? {
            if (instance == null) {
                instance = UserExerciseLineManager()
            }
            return instance
        }
    }

    suspend fun selectAll(): MutableList<UserExerciseLine> {
        val ret = mutableListOf<UserExerciseLine>()

        try {
            // Obtener todos los documentos de la colección "users"
            val userExerciseLines = ManagerFactory().getDB(COLLECTION_NAME).get().await()

            // Recorrer los documentos y convertirlos en objetos User
            for (document in userExerciseLines) {
                val userExerciseLine = document.toObject(UserExerciseLine::class.java)
                ret.add(
                    UserExerciseLine(
                        userExerciseLine.userId,
                        userExerciseLine.exerciseId
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
    }

    suspend fun insert(userExerciseLine: UserExerciseLine) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME)
                .document(userExerciseLine.userId.toString() + "x" + userExerciseLine.exerciseId.toString())
                .set(
                    UserExerciseLine(
                        userExerciseLine.userId,
                        userExerciseLine.exerciseId
                    )
                )
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    suspend fun update(userExerciseLine: UserExerciseLine) {
        val userExerciseLineMap = hashMapOf<String, Any?>(
            "userId" to userExerciseLine.userId,
            "exerciseId" to userExerciseLine.exerciseId,
        )

        ManagerFactory().getDB(COLLECTION_NAME)
            .document(userExerciseLine.userId.toString() + "x" + userExerciseLine.exerciseId.toString())
            .update(userExerciseLineMap)
            .await()
    }

    suspend fun delete(userExerciseLine: UserExerciseLine) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME)
                .document(userExerciseLine.userId.toString() + "x" + userExerciseLine.exerciseId.toString())
                .delete()
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}