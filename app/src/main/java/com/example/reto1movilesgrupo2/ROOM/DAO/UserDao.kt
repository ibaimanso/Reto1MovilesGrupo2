package com.example.reto1movilesgrupo2.ROOM.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reto1movilesgrupo2.ROOM.entities.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun selectUser() : List<User>

    @Insert
    suspend fun insert(vararg user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}