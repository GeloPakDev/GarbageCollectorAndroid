package com.example.garbagecollector.repository.web.dto

data class RegistrationDto(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val password: String? = null
)