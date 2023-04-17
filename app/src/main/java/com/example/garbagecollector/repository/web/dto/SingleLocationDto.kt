package com.example.garbagecollector.repository.web.dto


data class SingleLocationDto(
    val data: LocationDto? = null,
    val message: String? = null,
    val status: Int? = null
)