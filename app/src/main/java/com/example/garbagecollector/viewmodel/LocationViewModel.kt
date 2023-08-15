package com.example.garbagecollector.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.location.Address
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.garbagecollector.model.State
import com.example.garbagecollector.repository.web.LocationRepository
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.repository.web.UserRepository
import com.example.garbagecollector.repository.web.dto.LocationRequestDto
import com.example.garbagecollector.repository.web.dto.LocationResponseDto
import com.example.garbagecollector.util.network.NetworkConnectivity
import com.example.garbagecollector.util.network.ResponseHandler
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val userRepository: UserRepository,
    application: Application
) : AndroidViewModel(application) {
    //Remote
    var remoteLocations = MutableLiveData<NetworkResult<List<LocationResponseDto>>>()
    var remoteTotalLocationsNumber = MutableLiveData<NetworkResult<Int>>()

    fun getAuthInstance() = locationRepository.getAuthInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addLocation(latLng: LatLng, address: Address?, garbagePhoto: Bitmap) {
        val location = LocationRequestDto()
        location.name = address?.featureName ?: ""
        location.city = address?.locality ?: ""
        location.postalCode = address?.postalCode?.toInt() ?: 0
        location.latitude = latLng.latitude
        location.longitude = latLng.longitude
        //Convert the Bitmap to byte array
        val stream = ByteArrayOutputStream()
        garbagePhoto.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        // encode the byte array to a base64 string
        location.photo = byteArray
        location.state = State.NEW
        location.creationDate = LocalDate.now().toString()
        location.claimDate = null
        location.postedUserId = null
        location.claimedUserId = null
        locationRepository.addLocation(location)
    }

    suspend fun claimLocation(id: String) {
        locationRepository.claimLocation(id)
        userRepository.updateUserCoins(userRepository.getAuthInstance().currentUser!!.uid, 100)
    }

    suspend fun getLocationById(id: String) = withContext(Dispatchers.IO) {
        locationRepository.getLocationById(id)
    }

    fun getAllLiveLocations() = viewModelScope.launch {
        getSafeLocationsCall()
        Log.d("TAG", "getAllLiveLocations: " + remoteLocations.value)
    }

    private suspend fun getSafeLocationsCall() {
        remoteLocations.value = NetworkResult.Loading()
        if (NetworkConnectivity.hasInternetConnection(getApplication())) {
            try {
                val response = locationRepository.getLocations()
                Log.d("TAG", "getSafeLocationsCall: $response")
                remoteLocations.value = ResponseHandler.handleLocationsResponse(response)
            } catch (e: Exception) {
                remoteLocations.value = NetworkResult.Error("Locations not found")
            }
        } else {
            remoteLocations.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    fun getTotalPostedLocations() =
        viewModelScope.launch {
            getTotalPostedLocationsNumber()
        }

    private suspend fun getTotalPostedLocationsNumber() {
        remoteTotalLocationsNumber.value = NetworkResult.Loading()

        if (NetworkConnectivity.hasInternetConnection(getApplication())) {
            try {
                //Here use Firebase to get the total number of posted locations
                val response = locationRepository.getTotalLocationsNumber()
                remoteTotalLocationsNumber.value =
                    ResponseHandler.handlePostedLocationsNumberResponse(response)
            } catch (e: Exception) {
                remoteTotalLocationsNumber.value = NetworkResult.Error("Locations not found")
            }
        } else {
            remoteTotalLocationsNumber.value = NetworkResult.Error("No Internet Connection.")
        }
    }
}