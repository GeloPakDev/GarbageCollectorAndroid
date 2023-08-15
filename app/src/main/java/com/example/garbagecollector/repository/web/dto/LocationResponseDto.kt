package com.example.garbagecollector.repository.web.dto

import com.example.garbagecollector.model.State

data class LocationResponseDto(
    var id: String = "",
    var name: String? = "",
    var city: String? = "",
    var postalCode: Int? = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var photo: String = "",
    var state: State = State.NEW,
    var creationDate: String? = null,
    var claimDate: String? = null,
    var postedUserId: String? = null,
    var claimedUserId: String? = null
) {
}