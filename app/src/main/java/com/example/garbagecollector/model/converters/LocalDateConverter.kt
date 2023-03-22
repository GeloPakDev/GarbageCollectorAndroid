package com.example.garbagecollector.model.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLong(epoch: Long?): LocalDate? {
        return if (epoch == null) null else LocalDate.ofEpochDay(epoch)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun localDateToEpoch(localDate: LocalDate?): Long? {
        return localDate?.toEpochDay()
    }
}