package com.example.garbagecollector.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.garbagecollector.repository.LocationRepository
import com.google.android.gms.maps.model.Marker

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MapsViewModel"
    private val locationRepository: LocationRepository = LocationRepository(getApplication())

    fun addLocation(marker: Marker, image: Bitmap?) {
        val bookmark = locationRepository.createLocation()
        bookmark.placeId = marker.id
        bookmark.name = marker.title.toString()
        bookmark.longitude = marker.position.longitude
        bookmark.latitude = marker.position.latitude

        val newId = locationRepository.addLocation(bookmark)
        Log.i(TAG, "New bookmark $newId added to the database.")
    }
}