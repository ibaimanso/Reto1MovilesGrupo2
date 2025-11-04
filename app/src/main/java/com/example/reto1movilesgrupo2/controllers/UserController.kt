package com.example.reto1movilesgrupo2.controllers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reto1movilesgrupo2.entities.User
import com.example.reto1movilesgrupo2.firebase.ManagerFactory
import com.example.reto1movilesgrupo2.firebase.exception.DBException


class UserController : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private suspend fun nextId(): Int {
        val users: MutableList<User> = selectAll()
        var maxId = 0;

        for (user: User in users) {
            if (user.id > maxId) {
                maxId = user.id
            }
        }

        return maxId + 1
    }

    suspend fun selectAll(): MutableList<User> {
        return ManagerFactory().getUserManager().selectAll()
    }

    suspend fun insert(newUser: User) {
        newUser.id = nextId()
        ManagerFactory().getUserManager().insert(newUser)
    }

    suspend fun update(userToUpdate: User) {
        ManagerFactory().getUserManager().update(userToUpdate)
    }

    suspend fun delete(userToDelete: User) {
        ManagerFactory().getUserManager().delete(userToDelete)
    }

    suspend fun existUser(userToCheck: User): Boolean {
        val users: MutableList<User> = selectAll()

        for (user: User in users) {
            if (userToCheck.fname.equals(user.fname)) return true
        }

        return false
    }

    suspend fun existLogin(userToCheck: User): Boolean {
        val users: MutableList<User> = selectAll()

        for (user: User in users) {
            if (!userToCheck.fname.equals(user.fname)) continue

            return userToCheck.pw.equals(user.pw)
        }

        return false
    }

    suspend fun selectByFname(userToFind: User): User? {
        var ret: User? = null
        val users: MutableList<User> = selectAll()

        for (user: User in users) {
            if (user.fname.equals(userToFind.fname)) {
                ret = user
                break
            }
        }

        return ret
    }
}