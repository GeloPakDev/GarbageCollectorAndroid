package com.example.garbagecollector.db.model.converters

import androidx.room.TypeConverter
import com.example.garbagecollector.db.model.State

class StateConverter {
    @TypeConverter
    fun toState(value: String) = enumValueOf<State>(value)

    @TypeConverter
    fun fromState(value: State) = value.name
}