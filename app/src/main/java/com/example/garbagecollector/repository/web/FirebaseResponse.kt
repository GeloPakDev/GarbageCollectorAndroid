package com.example.garbagecollector.repository.web

sealed class FirebaseResponse<out T> {
    data class Success<out T>(val data: T) : FirebaseResponse<T>()
    data class Error(val exception: Exception) : FirebaseResponse<Nothing>()
}
