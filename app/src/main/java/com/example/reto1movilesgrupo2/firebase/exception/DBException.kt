package com.example.reto1movilesgrupo2.firebase.exception

class DBException : Exception {

    companion object {
        private const val serialVersionUID: Long = 1L
    }

    constructor() : super()

    constructor(message: String) : super(message)

}