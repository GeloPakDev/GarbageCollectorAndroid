package com.example.garbagecollector.repository

import com.example.garbagecollector.repository.database.Location
import com.example.garbagecollector.repository.database.LocationDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataStore @Inject constructor(
    private val locationDao: LocationDao
) {
    fun findAllLocations(): Flow<List<Location>> {
        return locationDao.findAllNewLocations()
    }

    fun insertLocations(locations: List<Location>) {
        locationDao.insertLocations(locations)
    }
}