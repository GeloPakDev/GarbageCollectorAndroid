package com.example.garbagecollector.repository.web.dto

import com.example.garbagecollector.model.User


data class UserDto(
    val data: User? = null,
    val message: String? = null,
    val statusCode: Int? = null
)