package com.example.garbagecollector.model

import android.graphics.Bitmap

data class User(
    var id: Long? = null,
    var email: String = "",
    var password: String = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var city: String? = "",
    var district: String? = "",
    var photo: Bitmap? = null,
    var points: Long = 0,
    var ranking: Rank = Rank.NEW,
    var role: Role = Role.USER
)