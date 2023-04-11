package com.example.garbagecollector.api.dto

import com.example.garbagecollector.model.State

data class LocationDto(
    var id: Long? = null,
    var name: String? = "",
    var city: String? = "",
    var postalCode: Int? = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var photo: String? = null,
    var state: State = State.NEW,
    var creationDate: String? = null,
    var claimDate: String? = null,
    var postedUserId: Long? = null,
    var claimedUserId: Long? = null
)
