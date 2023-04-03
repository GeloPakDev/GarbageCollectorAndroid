package com.example.garbagecollector.api

import com.example.garbagecollector.api.dto.LoginDto
import com.example.garbagecollector.api.dto.LoginJWTDto
import com.example.garbagecollector.api.dto.RegistrationDto
import com.example.garbagecollector.db.model.Location
import com.example.garbagecollector.db.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LocationApi {

    @GET("api/locations")
    suspend fun getLocations(): List<Location>

    @POST("api/locations")
    suspend fun postLocation(@Body loginDto: LoginDto): Location

    @POST("api/register")
    suspend fun registerUser(@Body registrationDto: RegistrationDto): User

    @POST("api/auth")
    suspend fun loginUser(@Body loginDto: LoginDto): LoginJWTDto
}