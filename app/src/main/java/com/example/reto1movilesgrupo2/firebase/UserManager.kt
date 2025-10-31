package com.example.reto1movilesgrupo2.firebase

import com.example.reto1movilesgrupo2.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserManager {

    private val COLLECTION_NAME: String = "users"

    companion object {
        private var instance: UserManager? = null

        fun getInstance(): UserManager? {
            if (instance == null) {
                instance = UserManager()
            }
            return instance
        }
    }

    suspend fun selectAll(): MutableList<User> {
        val ret = mutableListOf<User>()

        try {
            // Obtener todos los documentos de la colección "users"
            val usuarios = ManagerFactory().getDB(COLLECTION_NAME).get().await()

            // Recorrer los documentos y convertirlos en objetos User
            for (document in usuarios) {
                val user = document.toObject(User::class.java)
                ret.add(User(
                    user.trainer,
                    user.id,
                    user.level,
                    user.fname,
                    user.lname,
                    user.pw,
                    user.email,
                    user.birth,
                    user.lastMod
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
    }

    suspend fun insert(user: User) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME).document(user.id.toString())
                .set(User(
                    user.trainer,
                    user.id,
                    user.level,
                    user.fname,
                    user.lname,
                    user.pw,
                    user.email,
                    user.birth,
                    user.lastMod
                ))
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    suspend fun update(user: User) {
        val userMap = hashMapOf<String, Any?>(
            "trainer" to user.trainer,
            "id" to user.id,
            "level" to user.level,
            "fname" to user.fname,
            "lname" to user.lname,
            "pw" to user.pw,
            "email" to user.email,
            "birth" to user.birth,
            "lastMod" to user.lastMod
        )

        ManagerFactory().getDB(COLLECTION_NAME).document(user.id.toString())
            .update(userMap)
            .await()
    }

    suspend fun delete(user: User) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME).document(user.id.toString())
                .delete()
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

}