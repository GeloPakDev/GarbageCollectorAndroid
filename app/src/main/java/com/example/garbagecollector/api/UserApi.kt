package com.example.garbagecollector.api

import com.example.garbagecollector.api.dto.LoginDto
import com.example.garbagecollector.api.dto.LoginJWTDto
import com.example.garbagecollector.api.dto.RegistrationDto
import com.example.garbagecollector.db.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @POST("api/register")
    suspend fun registerUser(@Body registrationDto: RegistrationDto): User

    @POST("api/auth")
    suspend fun loginUser(@Body loginDto: LoginDto): LoginJWTDto

    @GET("api/users")
    suspend fun getUserByEmail(@Query("email") email: String): User

}