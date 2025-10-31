package com.example.reto1movilesgrupo2.firebase

import com.example.reto1movilesgrupo2.entities.UserExerciseLine
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

    public fun getExerciseManager(): ExerciseManager {
        return ExerciseManager()
    }

    public fun getSerieManager(): SerieManager {
        return SerieManager()
    }

    public fun getUserExerciseLineManager(): UserExerciseLineManager {
        return UserExerciseLineManager()
    }

    public fun getUserManager(): UserManager {
        return UserManager()
    }

    public fun getUserSerieLineManager(): UserSerieLineManager {
        return UserSerieLineManager()
    }

    public fun getUserWorkoutLineManager(): UserWorkoutLineManager {
        return UserWorkoutLineManager()
    }

    public fun getWorkoutManager(): WorkoutManager {
        return WorkoutManager()
    }

}