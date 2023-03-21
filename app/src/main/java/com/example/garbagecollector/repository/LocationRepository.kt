package com.example.garbagecollector.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.garbagecollector.db.GarbageCollectorDatabase
import com.example.garbagecollector.db.LocationDao
import com.example.garbagecollector.model.Location

class LocationRepository(context: Context) {
    private val database = GarbageCollectorDatabase.getInstance(context)
    private val locationDao: LocationDao = database.locationDao()

    fun addLocation(location: Location): Long {
        val newId = locationDao.insertLocation(location)
        location.id = newId
        return newId
    }

    fun createLocation(): Location {
        return Location()
    }

    val allBookmarks: LiveData<List<Location>>
        get() {
            return locationDao.loadAll()
        }
}