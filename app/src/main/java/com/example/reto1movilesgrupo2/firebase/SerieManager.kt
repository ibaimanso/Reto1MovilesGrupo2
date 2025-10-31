package com.example.reto1movilesgrupo2.firebase

import com.example.reto1movilesgrupo2.entities.Serie
import com.example.reto1movilesgrupo2.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SerieManager {

    private val COLLECTION_NAME: String = "series"

    companion object {
        private var instance: SerieManager? = null

        fun getInstance(): SerieManager? {
            if (instance == null) {
                instance = SerieManager()
            }
            return instance
        }
    }

    suspend fun selectAll(): MutableList<Serie> {
        val ret = mutableListOf<Serie>()

        try {
            // Obtener todos los documentos de la colección "users"
            val series = ManagerFactory().getDB(COLLECTION_NAME).get().await()

            // Recorrer los documentos y convertirlos en objetos User
            for (document in series) {
                val serie = document.toObject(Serie::class.java)
                ret.add(Serie(
                    serie.id,
                    serie.exerciseId,
                    serie.expectedTime,
                    serie.restTime,
                    serie.repetitions,
                    serie.name,
                    serie.iconPath,
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
    }

    suspend fun insert(serie: Serie) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME).document(serie.id.toString())
                .set(Serie(
                    serie.id,
                    serie.exerciseId,
                    serie.expectedTime,
                    serie.restTime,
                    serie.repetitions,
                    serie.name,
                    serie.iconPath,
                ))
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    suspend fun update(serie: Serie) {
        val serieMap = hashMapOf<String, Any?>(
            "id" to serie.id,
            "exerciseId" to serie.exerciseId,
            "expectedTime" to serie.expectedTime,
            "restTime" to serie.restTime,
            "repetitions" to serie.repetitions,
            "name" to serie.name,
            "iconPath" to serie.iconPath,
        )

        ManagerFactory().getDB(COLLECTION_NAME).document(serie.id.toString())
            .update(serieMap)
            .await()
    }

    suspend fun delete(serie: Serie) {
        try {
            ManagerFactory().getDB(COLLECTION_NAME).document(serie.id.toString())
                .delete()
                .await()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

}