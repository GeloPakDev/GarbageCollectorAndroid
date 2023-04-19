package com.example.garbagecollector.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import com.example.garbagecollector.mapper.LocationsMapper
import com.example.garbagecollector.repository.Repository
import com.example.garbagecollector.repository.database.model.PostedLocation
import com.example.garbagecollector.repository.local.DataStoreManager
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.repository.web.dto.LocationDto
import com.example.garbagecollector.util.NetworkConnectivity.Companion.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostedGarbageViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    //ROOM Database
    val postedLocalLocations: LiveData<List<PostedLocation>> =
        repository.localDataStore.findAllPostedLocations().asLiveData()

    private fun offlineCacheLocations(locations: List<PostedLocation>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataStore.insertPostedUserLocations(locations)
        }

    //Data Store
    private val dataStoreManager = DataStoreManager(application)
    val token = dataStoreManager.userTokenFlow.asLiveData()
    val userId = dataStoreManager.userId.asLiveData()

    //Remote
    var remoteLocations = MutableLiveData<NetworkResult<List<LocationDto>>>()

    fun getLocations() = viewModelScope.launch {
        getLocationsSafeCall()
    }


    @SuppressLint("NewApi")
    private suspend fun getLocationsSafeCall() {
        remoteLocations.value = NetworkResult.Loading()
        //Check Internet Connection
        if (hasInternetConnection(getApplication())) {
            try {
                val userId = runBlocking { dataStoreManager.userId.first() }
                //Get locations from the Azure Cloud
                val response = repository.remoteDataSource.getPostedUserLocations(userId)
                //Handle retrieved Response
                remoteLocations.value = handleLocationsResponse(response)

                val localLocationsForSave = remoteLocations.value
                if (localLocationsForSave != null) {
                    //Save retrieved locations to ROOM
                    offlineCacheLocations(
                        LocationsMapper.mapLocationDtoToPostedLocation(
                            localLocationsForSave.data
                        )
                    )
                }
            } catch (e: Exception) {
                remoteLocations.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            remoteLocations.value = NetworkResult.Error("No Internet Connection")
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