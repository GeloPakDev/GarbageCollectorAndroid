package com.example.garbagecollector.api.dto

import com.example.garbagecollector.model.User


data class UserDto(
    val data: User? = null,
    val message: String? = null,
    val statusCode: Int? = null
)