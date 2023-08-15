package com.example.garbagecollector.repository

import com.example.garbagecollector.repository.database.dao.ClaimedLocationDao
import com.example.garbagecollector.repository.database.dao.LocalLocationDao
import com.example.garbagecollector.repository.database.model.Location
import com.example.garbagecollector.repository.database.dao.LocationDao
import com.example.garbagecollector.repository.database.dao.PostedLocationDao
import com.example.garbagecollector.repository.database.model.ClaimedLocation
import com.example.garbagecollector.repository.database.model.LocalLocation
import com.example.garbagecollector.repository.database.model.PostedLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataStore @Inject constructor(
    private val locationDao: LocationDao,
    private val postedLocationDao: PostedLocationDao,
    private val claimedLocationDao: ClaimedLocationDao,
    private val localLocationDao: LocalLocationDao
) {
    //READ METHODS
    fun findAllLocalLocations(): List<LocalLocation> {
        return localLocationDao.findAllLocalLocations()
    }

    fun findAllClaimedLocations(): Flow<List<ClaimedLocation>> {
        return claimedLocationDao.findAllClaimedUserLocations()
    }

    fun findAllPostedLocations(): Flow<List<PostedLocation>> {
        return postedLocationDao.findAllPostedUserLocations()
    }

    fun findLocationById(locationId: Long): Location {
        return locationDao.findLocationById(locationId)
    }

    //CREATE METHODS
    fun insertLocation(location: Location) {
        locationDao.create(location)
    }

    fun insertClaimedUserLocations(claimedLocations: List<ClaimedLocation>) {
        claimedLocationDao.insertClaimedLocations(claimedLocations)
    }

    fun insertPostedUserLocations(postedLocations: List<PostedLocation>) {
        postedLocationDao.insertPostedLocations(postedLocations)
    }

    fun insertLocalLocations(localLocations: List<LocalLocation>) {
        localLocationDao.insertLocalLocations(localLocations)
    }

    //DELETE METHODS
    fun deleteLocationById(id: Long) {
        locationDao.deleteLocationById(id)
    }

//    fun deleteLocalLocationById(id: Long) {
//        localLocationDao.deleteLocalLocationById(id)
//    }
}