package com.example.garbagecollector.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var placeId: String? = null,
    var name: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
)