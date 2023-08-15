package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.garbagecollector.repository.web.UserRepository
import com.example.garbagecollector.repository.web.dto.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    application: Application
) :
    AndroidViewModel(application) {

    fun getAuthInstance() = repository.getAuthInstance()
    suspend fun getUserDetails(uid: String): ProfileDto? = repository.getUserDetails(uid)
    suspend fun updateUserDetails(userId: String, email: String, city: String, district: String) {
        val updateUserDto = UpdateUserDto(userId, email, city, district)
        repository.updateUserDetails(updateUserDto)
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.signOut()
        }
    }
}