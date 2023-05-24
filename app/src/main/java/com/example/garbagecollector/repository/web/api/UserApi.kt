package com.example.garbagecollector.repository.web.api

import com.example.garbagecollector.model.User
import com.example.garbagecollector.repository.web.dto.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @POST("api/register")
    suspend fun registerUser(@Body registrationDto: RegistrationDto): User

    @POST("api/auth")
    suspend fun loginUser(@Body loginDto: LoginDto): LoginJWTDto

    @GET("api/users")
    suspend fun getUserByEmail(@Query("email") email: String): Response<UserDto>

    @GET("api/users/total")
    suspend fun getUserPoints(@Query("userId") userId: Long): Int

    @GET("api/users/points")
    suspend fun getAllUsersByPoints(): Response<List<User>>

    @PATCH("api/users")
    suspend fun updateUser(@Body userDto: UpdateUserDto, @Query("userId") userId: Long)

}