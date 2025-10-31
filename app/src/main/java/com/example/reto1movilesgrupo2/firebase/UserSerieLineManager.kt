package com.example.reto1movilesgrupo2.firebase

import com.example.reto1movilesgrupo2.entities.UserSerieLine
import com.example.reto1movilesgrupo2.entities.UserWorkoutLine
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserSerieLineManager {

    private val COLLECTION_NAME: String = "userSerieLines"

    companion object {
        private var instance: UserSerieLineManager? = null

        fun getInstance(): UserSerieLineManager? {
            if (instance == null) {
                instance = UserSerieLineManager()
            }
            return instance
        }
    }

    suspend fun selectAll(): MutableList<UserSerieLine> {
        val ret = mutableListOf<UserSerieLine>()

        try {
            // Obtener todos los documentos de la colección "users"
            val userSerieLines = ManagerFactory().getDB(COLLECTION_NAME).get().await()

            // Recorrer los documentos y convertirlos en objetos User
            for (document in userSerieLines) {
                val userSerieLine = document.toObject(UserSerieLine::class.java)
                ret.add(UserSerieLine(
                    userSerieLine.userId,
                    userSerieLine.serieId,
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
    }

    suspend fun insert(userSerieLine: UserSerieLine) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME)
                .document(userSerieLine.userId.toString() + "x" + userSerieLine.serieId.toString())
                .set(UserSerieLine(
                    userSerieLine.userId,
                    userSerieLine.serieId,
                ))
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    suspend fun update(userSerieLine: UserSerieLine) {
        val userSerieLineMap = hashMapOf<String, Any?>(
            "userId" to userSerieLine.userId,
            "serieId" to userSerieLine.serieId,
        )

        ManagerFactory().getDB(COLLECTION_NAME)
            .document(userSerieLine.userId.toString() + "x" + userSerieLine.serieId.toString())
            .update(userSerieLineMap)
            .await()
    }

    suspend fun delete(userSerieLine: UserSerieLine) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME)
                .document(userSerieLine.userId.toString() + "x" + userSerieLine.serieId.toString())
                .delete()
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

}