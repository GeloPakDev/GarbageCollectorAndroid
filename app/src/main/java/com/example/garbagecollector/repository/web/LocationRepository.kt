package com.example.garbagecollector.repository.web

import com.example.garbagecollector.repository.web.dto.LocationRequestDto
import com.example.garbagecollector.repository.web.dto.LocationResponseDto
import com.google.firebase.auth.FirebaseAuth

interface LocationRepository {
    suspend fun getLocationById(id: String): LocationResponseDto
    suspend fun getTotalLocationsNumber(): FirebaseResponse<Int>
    suspend fun getUserPostedLocations(userId: String): FirebaseResponse<List<LocationResponseDto>>
    suspend fun getUserClaimedLocations(userId: String): FirebaseResponse<List<LocationResponseDto>>
    suspend fun getTotalPostedUserLocations(userId: String): Int
    suspend fun getTotalClaimedUserLocations(userId: String): Int
    suspend fun getUserPoints(userId: String): Int
    suspend fun addLocation(locationRequestDto: LocationRequestDto)
    suspend fun claimLocation(id: String)
    suspend fun getLocations(): FirebaseResponse<List<LocationResponseDto>>
    fun getAuthInstance(): FirebaseAuth
}