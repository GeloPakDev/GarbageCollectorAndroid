package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.garbagecollector.repository.Repository
import com.example.garbagecollector.repository.web.dto.LoginDto
import com.example.garbagecollector.repository.web.dto.LoginJWTDto
import com.example.garbagecollector.repository.web.dto.RegistrationDto
import com.example.garbagecollector.repository.web.dto.UserDto

import com.example.garbagecollector.repository.local.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) :
    AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application)
    val token = dataStoreManager.userTokenFlow.asLiveData()
    val email = dataStoreManager.userEmail.asLiveData()


    fun registerUser(registrationDto: RegistrationDto) {
        viewModelScope.launch(Dispatchers.IO) {
            //Send request to register the user
            repository.remoteDataSource.registerUser(registrationDto)
        }
    }

    fun loginUser(loginDto: LoginDto) {
        viewModelScope.launch(Dispatchers.IO) {
            //Send request to sign in user and retrieve JWT token and email
            val loginJWTDto = repository.remoteDataSource.loginUser(loginDto)
            //Save JWT token to the Local DataStore to make subsequent requests
            saveCredentialsToDataStore(loginJWTDto)
        }
    }

    suspend fun findUserByEmail(email: String): UserDto =
        withContext(Dispatchers.IO) {
            val user = repository.remoteDataSource.getUserByEmail(email)
            user.data?.id?.let { saveUserIdToDataStore(it) }
            user
        }


    private fun saveCredentialsToDataStore(loginJWTDto: LoginJWTDto) {
        viewModelScope.launch(Dispatchers.IO) {
            //Saves JWT token and email to the database
            dataStoreManager.saveUserCredentials(loginJWTDto)
        }
    }

    private fun saveUserIdToDataStore(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.saveUserId(userId)
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.clearUserCredentials()
        }
    }
}