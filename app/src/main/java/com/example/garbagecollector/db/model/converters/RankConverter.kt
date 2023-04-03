package com.example.garbagecollector.db.model.converters

import androidx.room.TypeConverter
import com.example.garbagecollector.db.model.Rank

class RankConverter {
    @TypeConverter
    fun toRank(value: String) = enumValueOf<Rank>(value)

    @TypeConverter
    fun fromRank(value: Rank) = value.name
}