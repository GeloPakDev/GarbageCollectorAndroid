package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.garbagecollector.model.User
import com.example.garbagecollector.repository.Repository
import com.example.garbagecollector.repository.web.NetworkResult
import com.example.garbagecollector.util.NetworkConnectivity
import com.example.garbagecollector.util.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    var remoteUsers = MutableLiveData<NetworkResult<List<User>>>()
    fun getAllUsers() = viewModelScope.launch {
        getSafeUsersCall()
    }

    private suspend fun getSafeUsersCall() {
        remoteUsers.value = NetworkResult.Loading()
        if (NetworkConnectivity.hasInternetConnection(getApplication())) {
            try {
                val response = repository.remoteDataSource.getAllUsersByPoints()
                remoteUsers.value = ResponseHandler.handleUsersResponse(response)
            } catch (e: Exception) {
                remoteUsers.value = NetworkResult.Error("Users not found")
            }
        } else {
            remoteUsers.value = NetworkResult.Error("No Internet Connection.")
        }
    }
}