package com.example.garbagecollector.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.garbagecollector.repository.database.converters.BitmapConverter
import com.example.garbagecollector.repository.database.converters.LocalDateConverter

@Database(entities = [Location::class], version = 1, exportSchema = false)
@TypeConverters(
    BitmapConverter::class,
    LocalDateConverter::class,
)
abstract class GarbageCollectorDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}