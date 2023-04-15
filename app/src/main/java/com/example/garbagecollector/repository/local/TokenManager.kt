package com.example.garbagecollector.repository.local

interface TokenManager {
    fun getAuthToken(): String?
    fun setAuthToken(token: String)

    fun getUserEmail(): String?
    fun setUserEmail(email: String)

    fun getUserId(): Long?
    fun setUserId(id: Long)
}