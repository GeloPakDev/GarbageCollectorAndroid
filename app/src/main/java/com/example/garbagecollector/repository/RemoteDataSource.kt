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
    //GET METHODS
    suspend fun getLocations(): Response<List<Location>> {
        return locationApi.getLocations()
    }

    suspend fun getTotalPostedLocations(): Response<Int> {
        return locationApi.getTotalPostedLocations()
    }

    suspend fun getTotalPostedUserLocations(userId: Long): Int {
        return locationApi.getTotalPostedUserLocations(userId)
    }

    suspend fun getTotalClaimedUserLocations(userId: Long): Int {
        return locationApi.getTotalClaimedUserLocations(userId)
    }

    suspend fun getClaimedUserLocations(userId: Long): Response<List<LocationDto>> {
        return locationApi.getClaimedUserLocations(userId)
    }

    suspend fun getPostedUserLocations(userId: Long): Response<List<LocationDto>> {
        return locationApi.getPostedUserLocations(userId)
    }

    suspend fun getLocationById(locationId: Long): SingleLocationDto {
        return locationApi.getLocationById(locationId)
    }

    suspend fun getUserByEmail(email: String): Response<UserDto> {
        return userApi.getUserByEmail(email)
    }

    //POST METHODS
    suspend fun postLocation(location: LocationDto): LocationDto {
        return locationApi.postLocation(location)
    }

    suspend fun registerUser(registrationDto: RegistrationDto): User {
        return userApi.registerUser(registrationDto)
    }

    suspend fun loginUser(loginDto: LoginDto): LoginJWTDto {
        return userApi.loginUser(loginDto)
    }

    //PATCH METHODS
    suspend fun claimLocation(
        id: Long,
        claimedUserId: Long
    ) {
        locationApi.claimLocation(id, claimedUserId)
    }
}