package com.example.garbagecollector.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.garbagecollector.util.Constants

@Entity(tableName = Constants.LOCAL_LOCATION_TABLE_NAME)
data class LocalLocation(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var longitude: Double = 0.0,
    var latitude: Double = 0.0
)