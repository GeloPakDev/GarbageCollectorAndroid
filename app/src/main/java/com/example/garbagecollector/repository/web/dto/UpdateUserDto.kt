package com.example.garbagecollector.repository.web.dto

data class UpdateUserDto(
    val email: String,
    val city: String,
    val district: String
)