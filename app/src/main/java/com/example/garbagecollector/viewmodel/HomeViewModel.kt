package com.example.garbagecollector.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.garbagecollector.repository.web.dto.LocationDto
import com.example.garbagecollector.model.State
import com.example.garbagecollector.repository.local.DataStoreManager
import com.example.garbagecollector.repository.Repository
import com.example.garbagecollector.repository.web.NetworkResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) :
    AndroidViewModel(application) {
    private val dataStoreManager = DataStoreManager(application)
    val token = dataStoreManager.userTokenFlow.asLiveData()
    val userId = dataStoreManager.userId.asLiveData()

    var locationsResponse = MutableLiveData<NetworkResult<List<LocationDto>>>()


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

    suspend fun claimLocation(locationId: Long, claimedUserId: Long) {
        repository.remoteDataSource.claimLocation(locationId, claimedUserId)
    }


    fun getAllLiveLocations() = viewModelScope.launch {
        getSafeLocationsCall()
    }

    @SuppressLint("MissingPermission")
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private suspend fun getSafeLocationsCall() {
        locationsResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remoteDataSource.getLocations()
                locationsResponse.value = handleLocationsResponse(response)
            } catch (e: Exception) {
                locationsResponse.value = NetworkResult.Error("Locations not found")
            }
        } else {
            locationsResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleLocationsResponse(response: Response<List<LocationDto>>): NetworkResult<List<LocationDto>> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited")
            }
            response.body()!!.isEmpty() -> {
                return NetworkResult.Error("Locations Not Found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }
}