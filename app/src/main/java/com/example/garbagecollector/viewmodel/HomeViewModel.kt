package com.example.garbagecollector.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.location.Address
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.garbagecollector.mapper.LocationsMapper
import com.example.garbagecollector.repository.web.dto.LocationDto
import com.example.garbagecollector.model.State
import com.example.garbagecollector.repository.local.DataStoreManager
import com.example.garbagecollector.repository.Repository
import com.example.garbagecollector.repository.database.model.LocalLocation
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.repository.web.dto.Location
import com.example.garbagecollector.repository.web.dto.SingleLocationDto
import com.example.garbagecollector.util.network.NetworkConnectivity.Companion.hasInternetConnection
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
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) :
    AndroidViewModel(application) {
    //Local
    var postedLocalLocations = MutableLiveData<List<LocalLocation>>()

    //Data Store
    private val dataStoreManager = DataStoreManager(application)
    val token = dataStoreManager.userTokenFlow.asLiveData()
    val userId = dataStoreManager.userId.asLiveData()

    //Remote
    var remoteLocations = MutableLiveData<NetworkResult<List<Location>>>()
    var remoteTotalLocationsNumber = MutableLiveData<NetworkResult<Int>>()


    //Remote Calls
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addLocation(latLng: LatLng, address: Address?, garbagePhoto: Bitmap) {
        val location = LocationDto()
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
        location.photo = Base64.encodeToString(byteArray, Base64.DEFAULT)
        location.state = State.NEW
        location.creationDate = LocalDate.now().toString()
        location.claimDate = null
        location.postedUserId = userId.value
        location.claimedUserId = null

        repository.remoteDataSource.postLocation(location)
    }

    suspend fun getLocationById(locationId: Long): SingleLocationDto =
        withContext(Dispatchers.IO) {
            repository.remoteDataSource.getLocationById(locationId)
        }

    fun getAllLiveLocations() = viewModelScope.launch {
        getSafeLocationsCall()
    }

    fun getTotalPostedLocations() =
        viewModelScope.launch {
            getTotalPostedLocationsNumber()
        }


    private suspend fun getTotalPostedLocationsNumber() {
        remoteTotalLocationsNumber.value = NetworkResult.Loading()

        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remoteDataSource.getTotalPostedLocations()
                remoteTotalLocationsNumber.value =
                    ResponseHandler.handlePostedLocationsNumberResponse(response)
            } catch (e: Exception) {
                remoteTotalLocationsNumber.value = NetworkResult.Error("Locations not found")
            }
        } else {
            remoteTotalLocationsNumber.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getSafeLocationsCall() {
        remoteLocations.value = NetworkResult.Loading()
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remoteDataSource.getLocations()
                remoteLocations.value = ResponseHandler.handleLocationsResponse(response)
                val localLocationsForSave = remoteLocations.value
                if (localLocationsForSave != null) {
                    //Save retrieved locations to ROOM
                    offlineCacheLocations(
                        LocationsMapper.mapLocationToLocalLocation(
                            localLocationsForSave.data
                        )
                    )
                }
            } catch (e: Exception) {
                remoteLocations.value = NetworkResult.Error("Locations not found")
            }
        } else {
            remoteLocations.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    //Local Calls
    fun addLocalLocation(location: com.example.garbagecollector.repository.database.model.Location) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataStore.insertLocation(location)
        }
    }

    suspend fun claimLocation(locationId: Long, claimedUserId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataStore.deleteLocalLocationById(locationId)
            repository.localDataStore.deleteLocationById(locationId)
        }
        repository.remoteDataSource.claimLocation(locationId, claimedUserId)
    }

    suspend fun getLocalLocationById(locationId: Long): com.example.garbagecollector.repository.database.model.Location =
        withContext(Dispatchers.IO) {
            repository.localDataStore.findLocationById(locationId)
        }

    fun getAllLocalLocations() = viewModelScope.launch {
        val locations = getLocalLocations()
        postedLocalLocations.value = locations
    }

    private suspend fun getLocalLocations(): List<LocalLocation> = withContext(Dispatchers.IO) {
        repository.localDataStore.findAllLocalLocations()
    }

    private fun offlineCacheLocations(locations: List<LocalLocation>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataStore.insertLocalLocations(locations)
        }
}
