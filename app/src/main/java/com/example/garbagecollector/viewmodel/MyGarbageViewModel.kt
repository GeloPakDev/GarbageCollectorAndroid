package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.garbagecollector.repository.Repository
import com.example.garbagecollector.repository.web.dto.LocationDto
import com.example.garbagecollector.repository.local.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyGarbageViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application)
    val token = dataStoreManager.userTokenFlow.asLiveData()
    val userId = dataStoreManager.userId.asLiveData()


    suspend fun getClaimedUserLocations(userId: Long): List<LocationDto> =
        withContext(Dispatchers.IO)
        {
            repository.remoteDataSource.getClaimedUserLocations(userId)
        }

    suspend fun getPostedUserLocations(userId: Long): List<LocationDto> =
        withContext(Dispatchers.IO)
        {
            repository.remoteDataSource.getPostedUserLocations(userId)
        }
}