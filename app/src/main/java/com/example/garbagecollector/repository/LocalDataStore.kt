package com.example.garbagecollector.repository

import com.example.garbagecollector.repository.database.dao.ClaimedLocationDao
import com.example.garbagecollector.repository.database.model.Location
import com.example.garbagecollector.repository.database.dao.LocationDao
import com.example.garbagecollector.repository.database.dao.PostedLocationDao
import com.example.garbagecollector.repository.database.model.ClaimedLocation
import com.example.garbagecollector.repository.database.model.PostedLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataStore @Inject constructor(
    private val locationDao: LocationDao,
    private val postedLocationDao: PostedLocationDao,
    private val claimedLocationDao: ClaimedLocationDao
) {
    fun findAllLocations(): Flow<List<Location>> {
        return locationDao.findAllNewLocations()
    }

    fun findAllClaimedLocations(): Flow<List<ClaimedLocation>> {
        return claimedLocationDao.findAllClaimedUserLocations()
    }

    fun findAllPostedLocations(): Flow<List<PostedLocation>> {
        return postedLocationDao.findAllPostedUserLocations()
    }

    fun insertLocations(locations: List<Location>) {
        locationDao.insertLocations(locations)
    }

    fun insertClaimedUserLocations(claimedLocations: List<ClaimedLocation>) {
        claimedLocationDao.insertClaimedLocations(claimedLocations)
    }

    fun insertPostedUserLocations(postedLocations: List<PostedLocation>) {
        postedLocationDao.insertPostedLocations(postedLocations)
    }
}