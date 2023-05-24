package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.garbagecollector.repository.Repository
import com.example.garbagecollector.repository.local.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyRankingViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) :
    AndroidViewModel(application) {
    //Data Store
    private val dataStoreManager = DataStoreManager(application)
    val token = dataStoreManager.userTokenFlow.asLiveData()

    var statistics = MutableLiveData<Map<String, Int>>()

    private suspend fun getTotalPostedLocations(): Int = withContext(Dispatchers.IO) {
        val userId = runBlocking { dataStoreManager.userId.first() }
        repository.remoteDataSource.getTotalPostedUserLocations(userId)
    }

    private suspend fun getTotalClaimedLocations(): Int = withContext(Dispatchers.IO) {
        val userId = runBlocking { dataStoreManager.userId.first() }
        repository.remoteDataSource.getTotalClaimedUserLocations(userId)
    }

    private suspend fun getUserPoints(): Int = withContext(Dispatchers.IO) {
        val userId = runBlocking { dataStoreManager.userId.first() }
        repository.remoteDataSource.getUserPoints(userId)
    }

    fun getData() {
        viewModelScope.launch {
            statistics.value = getStatisticsData()
        }
    }

    private suspend fun getStatisticsData(): Map<String, Int> {
        val statistics = mutableMapOf<String, Int>()
        withContext(Dispatchers.IO) {
            val totalPoints = getUserPoints()
            val claimedLocations = getTotalClaimedLocations()
            val postedLocations = getTotalPostedLocations()
            statistics["totalPoints"] = totalPoints
            statistics["claimedLocations"] = claimedLocations
            statistics["postedLocations"] = postedLocations
        }
        return statistics
    }
}