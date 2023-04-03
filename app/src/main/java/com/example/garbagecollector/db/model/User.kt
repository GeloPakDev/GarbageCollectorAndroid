package com.example.garbagecollector.db.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var email: String = "",
    var password: String = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var city: String? = "",
    var district: Int? = 0,
    var photo: Bitmap? = null,
    var points: Long = 0,
    var ranking: Rank = Rank.NEW,
    var role: Role = Role.USER
)