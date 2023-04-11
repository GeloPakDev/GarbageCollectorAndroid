package com.example.garbagecollector.api

import com.example.garbagecollector.api.dto.LocationDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface LocationApi {

    @GET("api/locations")
    suspend fun getLocations(): List<LocationDto>

    @POST("api/locations")
    suspend fun postLocation(@Body location: LocationDto): LocationDto

    @GET("api/locations/claimed")
    suspend fun getClaimedUserLocations(@Query("userId") userId: Long): List<LocationDto>

    @GET("api/locations/posted")
    suspend fun getPostedUserLocations(@Query("userId") userId: Long): List<LocationDto>

    @PATCH("api/locations")
    suspend fun claimLocation(
        @Query("id") id: Long,
        @Query("claimedUserId") claimedUserId: Long
    )
}