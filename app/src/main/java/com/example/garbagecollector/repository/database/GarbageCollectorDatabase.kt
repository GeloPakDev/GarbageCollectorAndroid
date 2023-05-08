package com.example.garbagecollector.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.garbagecollector.repository.database.converters.BitmapConverter
import com.example.garbagecollector.repository.database.converters.LocalDateConverter
import com.example.garbagecollector.repository.database.dao.ClaimedLocationDao
import com.example.garbagecollector.repository.database.dao.LocalLocationDao
import com.example.garbagecollector.repository.database.dao.LocationDao
import com.example.garbagecollector.repository.database.dao.PostedLocationDao
import com.example.garbagecollector.repository.database.model.ClaimedLocation
import com.example.garbagecollector.repository.database.model.LocalLocation
import com.example.garbagecollector.repository.database.model.Location
import com.example.garbagecollector.repository.database.model.PostedLocation

@Database(
    entities = [Location::class, ClaimedLocation::class, PostedLocation::class, LocalLocation::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(
    BitmapConverter::class,
    LocalDateConverter::class,
)
abstract class GarbageCollectorDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun postedLocationDao(): PostedLocationDao
    abstract fun claimedLocationDao(): ClaimedLocationDao
    abstract fun localLocationDao(): LocalLocationDao
}