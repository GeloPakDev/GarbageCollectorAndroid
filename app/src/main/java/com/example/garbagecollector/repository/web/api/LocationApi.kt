package com.example.garbagecollector.repository.web.api

import com.example.garbagecollector.repository.web.dto.Location
import com.example.garbagecollector.repository.web.dto.LocationRequestDto
import com.example.garbagecollector.repository.web.dto.SingleLocationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface LocationApi {

    @GET("api/locations")
    suspend fun getLocations(): Response<List<Location>>

    @GET("api/locations/total")
    suspend fun getTotalPostedLocations(): Response<Int>

    @GET("api/locations/posted/total")
    suspend fun getTotalPostedUserLocations(@Query("userId") userId: Long): Int

    @GET("api/locations/claimed/total")
    suspend fun getTotalClaimedUserLocations(@Query("userId") userId: Long): Int

    @POST("api/locations")
    suspend fun postLocation(@Body location: LocationRequestDto): LocationRequestDto

    @GET("api/locations/claimed")
    suspend fun getClaimedUserLocations(@Query("userId") userId: Long): Response<List<LocationRequestDto>>

    @GET("api/locations/posted")
    suspend fun getPostedUserLocations(@Query("userId") userId: Long): Response<List<LocationRequestDto>>

    @PATCH("api/locations")
    suspend fun claimLocation(
        @Query("id") id: Long,
        @Query("claimedUserId") claimedUserId: Long
    )

    @GET("api/locations")
    suspend fun getLocationById(@Query("id") id: Long): SingleLocationDto

}