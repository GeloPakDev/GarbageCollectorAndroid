package com.example.garbagecollector.repository

import com.example.garbagecollector.model.User
import com.example.garbagecollector.repository.web.api.LocationApi
import com.example.garbagecollector.repository.web.api.UserApi
import com.example.garbagecollector.repository.web.dto.*
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val locationApi: LocationApi,
    private val userApi: UserApi
) {
    suspend fun getLocations(): Response<List<LocationDto>> {
        return locationApi.getLocations()
    }

    suspend fun postLocation(location: LocationDto): LocationDto {
        return locationApi.postLocation(location)
    }

    suspend fun getClaimedUserLocations(userId: Long): List<LocationDto> {
        return locationApi.getClaimedUserLocations(userId)
    }

    suspend fun getPostedUserLocations(userId: Long): List<LocationDto> {
        return locationApi.getPostedUserLocations(userId)
    }

    suspend fun claimLocation(
        id: Long,
        claimedUserId: Long
    ) {
        locationApi.claimLocation(id, claimedUserId)
    }

    suspend fun registerUser(registrationDto: RegistrationDto): User {
        return userApi.registerUser(registrationDto)
    }

    suspend fun loginUser(loginDto: LoginDto): LoginJWTDto {
        return userApi.loginUser(loginDto)
    }

    suspend fun getUserByEmail(email: String): UserDto {
        return userApi.getUserByEmail(email)
    }
}