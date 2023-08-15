package com.example.garbagecollector.viewmodel

import android.app.Application
import android.util.Log
import com.example.garbagecollector.util.network.NetworkConnectivity.Companion.hasInternetConnection
import androidx.lifecycle.*
import com.example.garbagecollector.repository.web.LocationRepository
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.repository.web.dto.LocationResponseDto
import com.example.garbagecollector.util.network.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClaimedGarbageViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    application: Application
) : AndroidViewModel(application) {
    var remoteLocations = MutableLiveData<NetworkResult<List<LocationResponseDto>>>()
    fun getAuthInstance() = locationRepository.getAuthInstance()

    fun getAllLiveLocations(userId: String) = viewModelScope.launch {
        getSafeLocationsCall(userId)
        Log.d("TAG", "getAllLiveLocations: " + remoteLocations.value)
    }

    private suspend fun getSafeLocationsCall(userId: String) {
        remoteLocations.value = NetworkResult.Loading()
        if (hasInternetConnection(getApplication())) {
            try {
                val response = locationRepository.getUserClaimedLocations(userId)
                Log.d("TAG", "getSafeLocationsCall: $response")
                remoteLocations.value = ResponseHandler.handleLocationsResponse(response)
            } catch (e: Exception) {
                remoteLocations.value = NetworkResult.Error("Locations not found")
            }
        } else {
            remoteLocations.value = NetworkResult.Error("No Internet Connection.")
        }
    }
}