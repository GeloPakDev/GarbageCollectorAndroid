package com.example.garbagecollector.db.model.converters

import androidx.room.TypeConverter
import com.example.garbagecollector.db.model.Role

class RoleConverter {
    @TypeConverter
    fun toRole(value: String) = enumValueOf<Role>(value)

    @TypeConverter
    fun fromRole(value: Role) = value.name
}