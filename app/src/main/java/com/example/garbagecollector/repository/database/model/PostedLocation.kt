package com.example.garbagecollector.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.garbagecollector.util.Constants

@Entity(tableName = Constants.POSTED_LOCATION_TABLE_NAME)
data class PostedLocation(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String? = "",
    var city: String? = "",
    var postalCode: Int? = 0,
    var photo: String? = null,
    var createDate: String? = null
)