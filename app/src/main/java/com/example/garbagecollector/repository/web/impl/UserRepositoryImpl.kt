package com.example.garbagecollector.repository.web.impl

import com.example.garbagecollector.repository.web.UserRepository
import com.example.garbagecollector.repository.web.dto.ProfileDto
import com.example.garbagecollector.repository.web.dto.UpdateUserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserRepository {
    override fun getAuthInstance() = auth

    override suspend fun getUserDetails(uuid: String): ProfileDto? = withContext(Dispatchers.IO) {
        try {
            val userSnapshot = firestore.collection("users").document(uuid).get().await()
            val profile = userSnapshot.data

            profile?.let {
                ProfileDto(
                    it["firstName"].toString(),
                    it["lastName"].toString(),
                    it["city"].toString(),
                    it["district"].toString()
                )
            }
        } catch (e: Exception) {
            throw Exception("Error getting user details")
        }
    }

    override suspend fun updateUserDetails(updateUserDto: UpdateUserDto) {
        firestore.collection("users").document(updateUserDto.userId).update(
            mapOf(
                "city" to updateUserDto.city,
                "district" to updateUserDto.district
            )
        ).await()
    }

    override suspend fun updateUserCoins(uuid: String, points: Int) {
        firestore.collection("users").document(uuid).update("points", points).await()
    }

    override fun signOut() {
        auth.signOut()
    }
}