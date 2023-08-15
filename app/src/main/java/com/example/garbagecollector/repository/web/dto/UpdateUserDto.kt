package com.example.garbagecollector.repository.web.dto

data class UpdateUserDto(
    val userId: String,
    val email: String,
    val city: String,
    val district: String
)