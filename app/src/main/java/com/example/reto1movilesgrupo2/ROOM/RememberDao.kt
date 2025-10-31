/*
package com.example.reto1movilesgrupo2.room


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RememberDao {
    @Query("SELECT * FROM remember WHERE id = 1 LIMIT 1")
    suspend fun getUser(): RememberUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(user: RememberUser)

    @Query("DELETE FROM remember")
    suspend fun clearAll()
}
*/