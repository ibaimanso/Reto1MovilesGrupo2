package com.example.reto1movilesgrupo2.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ManagerFactory {

    companion object {
        private var instance: ManagerFactory? = null

        fun getInstance(): ManagerFactory? {
            if (instance == null) {
                instance = ManagerFactory()
            }
            return instance
        }
    }

    public fun getDB(collectionName: String): CollectionReference {
        return FirebaseFirestore.getInstance().collection(collectionName)
    }

}