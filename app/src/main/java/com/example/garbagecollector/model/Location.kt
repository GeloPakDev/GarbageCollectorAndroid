package com.example.garbagecollector.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Location(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var name: String? = "",
    var city: String? = "",
    var postalCode: Int? = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var photo: Bitmap? = null,
    var state: State = State.NEW,
    var createDate: LocalDate? = null
)