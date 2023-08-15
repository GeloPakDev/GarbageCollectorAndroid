package com.example.garbagecollector.repository.web

import com.example.garbagecollector.repository.web.dto.ProfileDto
import com.example.garbagecollector.repository.web.dto.UpdateUserDto
import com.google.firebase.auth.FirebaseAuth

interface UserRepository {
    suspend fun getUserDetails(uuid: String): ProfileDto?
    suspend fun updateUserDetails(updateUserDto: UpdateUserDto)
    suspend fun updateUserCoins(uuid: String, points: Int)
    fun getAuthInstance(): FirebaseAuth
    fun signOut()
}