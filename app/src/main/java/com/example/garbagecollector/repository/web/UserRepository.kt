package com.example.garbagecollector.repository.web

import com.example.garbagecollector.repository.web.dto.ProfileDto
import com.google.firebase.auth.FirebaseAuth

interface UserRepository {
    suspend fun getUserDetails(uuid: String): ProfileDto?
    fun getAuthInstance(): FirebaseAuth
    fun signOut()
}