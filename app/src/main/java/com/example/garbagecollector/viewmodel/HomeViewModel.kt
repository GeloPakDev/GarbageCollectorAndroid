package com.example.garbagecollector.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.location.Address
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.garbagecollector.db.model.Location
import com.example.garbagecollector.repository.LocationRepository
import com.example.garbagecollector.util.Constants
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val locationRepository: LocationRepository = LocationRepository(getApplication())
    private var locations: LiveData<List<Location>>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addLocation(latLng: LatLng, address: Address?, garbagePhoto: Bitmap) {
        val location = locationRepository.createLocation()
        location.name = address?.featureName ?: ""
        location.city = address?.locality ?: ""
        location.postalCode = address?.postalCode?.toInt() ?: 0
        location.longitude = latLng.longitude
        location.latitude = latLng.latitude
        location.photo = garbagePhoto
        location.createDate = LocalDate.now()

        val newId = locationRepository.addLocation(location)
        Log.i(Constants.HOME_VIEW_MODEL_TAG, "New location $newId added to the database.")
    }

    suspend fun claimLocation(locationId: Long) {
        locationRepository.updateLocationStatus(locationId)
    }

    fun getMarkers(): LiveData<List<Location>>? {
        if (locations == null) {
            locations = locationRepository.allLocations
        }
        return locations
    }
}