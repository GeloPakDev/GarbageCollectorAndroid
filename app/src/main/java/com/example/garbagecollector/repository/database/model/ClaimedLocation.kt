package com.example.garbagecollector.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.garbagecollector.util.Constants

@Entity(tableName = Constants.CLAIMED_LOCATION_TABLE_NAME)
data class ClaimedLocation(
    @PrimaryKey
    var id: String = "",
    var name: String? = "",
    var city: String? = "",
    var postalCode: Int? = 0,
    var photo: String? = null,
    var claimDate: String? = null
)