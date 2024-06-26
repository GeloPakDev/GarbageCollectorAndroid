package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.garbagecollector.repository.Repository

import com.example.garbagecollector.repository.local.DataStoreManager
import com.example.garbagecollector.repository.web.dto.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Response
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

    suspend fun findUserByEmail(email: String): Response<UserDto> =
        withContext(Dispatchers.IO) {
            val user = repository.remoteDataSource.getUserByEmail(email)
            user.body()?.data?.id?.let { saveUserIdToDataStore(it) }
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

    fun updateUser(userDto: UpdateUserDto) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = runBlocking { dataStoreManager.userId.first() }
            repository.remoteDataSource.updateUser(userDto, userId)
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.clearUserCredentials()
        }
    }
}