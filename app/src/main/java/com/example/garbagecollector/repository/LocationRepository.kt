package com.example.garbagecollector.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.garbagecollector.db.GarbageCollectorDatabase
import com.example.garbagecollector.db.LocationDao
import com.example.garbagecollector.model.Location

class LocationRepository(context: Context) {
    private val database = GarbageCollectorDatabase.getInstance(context)
    private val locationDao: LocationDao = database.locationDao()

    val allLocations: LiveData<List<Location>>
        get() {
            return locationDao.findAll()
        }

    fun addLocation(location: Location): Long {
        val newId = locationDao.create(location)
        location.id = newId
        return newId
    }

    fun createLocation(): Location {
        return Location()
    }
}