package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.garbagecollector.repository.web.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyRankingViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    application: Application
) :
    AndroidViewModel(application) {

    var statistics = MutableLiveData<Map<String, Int>>()

    private suspend fun getTotalPostedLocations(): Int = withContext(Dispatchers.IO) {
        val userId = locationRepository.getAuthInstance().currentUser!!.uid
        locationRepository.getTotalPostedUserLocations(userId)
    }

    private suspend fun getTotalClaimedLocations(): Int = withContext(Dispatchers.IO) {
        val userId = locationRepository.getAuthInstance().currentUser!!.uid
        locationRepository.getTotalClaimedUserLocations(userId)
    }

    private suspend fun getUserPoints(): Int = withContext(Dispatchers.IO) {
        val userId = locationRepository.getAuthInstance().currentUser!!.uid
        locationRepository.getUserPoints(userId)
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