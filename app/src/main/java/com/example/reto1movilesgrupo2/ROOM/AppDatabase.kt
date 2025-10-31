/*
package com.example.reto1movilesgrupo2.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [RememberUser::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rememberDao(): RememberDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Recrear la tabla si es necesario
                database.execSQL("DROP TABLE IF EXISTS remember")
                database.execSQL("""
                    CREATE TABLE remember (
                        id INTEGER PRIMARY KEY NOT NULL,
                        username TEXT,
                        password TEXT,
                        remember INTEGER NOT NULL
                    )
                """)
            }
        }
    }
}
*/