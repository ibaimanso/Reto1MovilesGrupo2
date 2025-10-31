package com.example.reto1movilesgrupo2.firebase

import com.example.reto1movilesgrupo2.entities.User
import com.example.reto1movilesgrupo2.entities.UserWorkoutLine
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserWorkoutLineManager {

    private val COLLECTION_NAME: String = "userWorkoutLines"

    companion object {
        private var instance: UserWorkoutLineManager? = null

        fun getInstance(): UserWorkoutLineManager? {
            if (instance == null) {
                instance = UserWorkoutLineManager()
            }
            return instance
        }
    }

    suspend fun selectAll(): MutableList<UserWorkoutLine> {
        val ret = mutableListOf<UserWorkoutLine>()

        try {
            // Obtener todos los documentos de la colección "users"
            val userWorkoutLines = ManagerFactory().getDB(COLLECTION_NAME).get().await()

            // Recorrer los documentos y convertirlos en objetos User
            for (document in userWorkoutLines) {
                val userWorkoutLine = document.toObject(UserWorkoutLine::class.java)
                ret.add(UserWorkoutLine(
                    userWorkoutLine.userId,
                    userWorkoutLine.workoutId,
                    userWorkoutLine.doneDate
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
    }

    suspend fun insert(userWorkoutLine: UserWorkoutLine) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME)
                .document(userWorkoutLine.userId.toString() + "x" + userWorkoutLine.workoutId.toString())
                .set(UserWorkoutLine(
                    userWorkoutLine.userId,
                    userWorkoutLine.workoutId,
                    userWorkoutLine.doneDate
                ))
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    suspend fun update(userWorkoutLine: UserWorkoutLine) {
        val userWorkoutLineMap = hashMapOf<String, Any?>(
            "userId" to userWorkoutLine.userId,
            "workoutId" to userWorkoutLine.workoutId,
            "doneDate" to userWorkoutLine.doneDate
        )

        ManagerFactory().getDB(COLLECTION_NAME)
            .document(userWorkoutLine.userId.toString() + "x" + userWorkoutLine.workoutId.toString())
            .update(userWorkoutLineMap)
            .await()
    }

    suspend fun delete(userWorkoutLine: UserWorkoutLine) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME)
                .document(userWorkoutLine.userId.toString() + "x" + userWorkoutLine.workoutId.toString())
                .delete()
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

}