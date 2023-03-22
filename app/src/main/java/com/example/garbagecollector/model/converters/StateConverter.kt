package com.example.garbagecollector.model.converters

import androidx.room.TypeConverter
import com.example.garbagecollector.model.State

class StateConverter {
    @TypeConverter
    fun toState(value: String) = enumValueOf<State>(value)

    @TypeConverter
    fun fromState(value: State) = value.name
}