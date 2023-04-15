package com.example.garbagecollector.repository.web.dto

data class LoginJWTDto(
    val email: String? = null,
    val password: String? = null,
    val token: String? = null
)