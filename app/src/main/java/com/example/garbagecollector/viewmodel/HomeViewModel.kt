package com.example.garbagecollector.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.location.Address
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.garbagecollector.model.Location
import com.example.garbagecollector.repository.LocationRepository
import com.example.garbagecollector.util.Constants
import com.google.android.gms.maps.model.LatLng

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val locationRepository: LocationRepository = LocationRepository(getApplication())
    private var locations: LiveData<List<Location>>? = null

    fun addLocation(latLng: LatLng, address: Address, garbagePhoto: Bitmap) {
        val location = locationRepository.createLocation()
        location.name = address.featureName
        location.city = address.locality
        location.postalCode = address.postalCode.toInt()
        location.longitude = latLng.longitude
        location.latitude = latLng.latitude
        location.photo = garbagePhoto

        val newId = locationRepository.addLocation(location)
        Log.i(Constants.HOME_VIEW_MODEL_TAG, "New location $newId added to the database.")
    }


    fun getMarkers(): LiveData<List<Location>>? {
        if (locations == null) {
            locations = locationRepository.allLocations
        }
        return locations
    }

//    private fun mapLocationsToMarkerOptions() {
//        //Get Location Objects from the Repository and convert them to MarkerOptions
//        locations = locationRepository.allLocations.map { repoBookmarks ->
//            repoBookmarks.map { bookmark -> locationToMarker(bookmark) }
//        }
//    }
//
//    private fun locationToMarker(location: Location): MarkerOptions {
//        val latLng = LatLng(location.latitude, location.longitude)
//        return MarkerOptions().position(latLng)
//    }
}