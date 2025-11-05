package com.example.reto1prueba.ROOM

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reto1movilesgrupo2.ROOM.DAO.UserDao
import com.example.reto1movilesgrupo2.ROOM.entities.User

@Database(entities = [User::class], version = 1)
abstract class MyRoomDatabase : RoomDatabase() {

    companion object {

        private var instance : MyRoomDatabase? = null

        private val LOCK = Any()

        operator fun invoke (context: Context) = instance?: synchronized(LOCK){
            instance?:buildDatabase (context).also {instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            MyRoomDatabase::class.java,
            "myDataBase"
        ).build()
    }

    abstract fun getUserDao() : UserDao

}