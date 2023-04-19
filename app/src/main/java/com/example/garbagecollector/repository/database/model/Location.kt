package com.example.garbagecollector.repository.database.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.garbagecollector.model.State
import com.example.garbagecollector.util.Constants.Companion.LOCATION_TABLE_NAME
import java.time.LocalDate

@Entity(tableName = LOCATION_TABLE_NAME)
data class Location(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var name: String? = "",
    var city: String? = "",
    var postalCode: Int? = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var photo: Bitmap? = null,
    var state: State = State.NEW,
    var createDate: LocalDate? = null,
    var claimDate: LocalDate? = null,
    var postedUser: Long? = null,
    var claimedUser: Long? = null
)