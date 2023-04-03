package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.garbagecollector.api.RetrofitInstance
import com.example.garbagecollector.api.dto.LoginDto
import com.example.garbagecollector.api.dto.LoginJWTDto
import com.example.garbagecollector.api.dto.RegistrationDto

import com.example.garbagecollector.db.model.User
import com.example.garbagecollector.util.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DataStoreManager(application)
    val token = repository.userTokenFlow.asLiveData()
    val email = repository.userEmail.asLiveData()

    private val _user: MutableLiveData<User?> = MutableLiveData()
    val user: LiveData<User?>
        get() = _user


    fun registerUser(registrationDto: RegistrationDto) {
        viewModelScope.launch {
            //Send request to register the user
            val fetchedUser = RetrofitInstance.api.registerUser(registrationDto)
            //Save registered user's data
            _user.value = fetchedUser
        }
    }

    fun loginUser(loginDto: LoginDto) {
        viewModelScope.launch {
            //Send request to sign in user and retrieve JWT token and email
            val loginJWTDto = RetrofitInstance.api.loginUser(loginDto)
            //Save JWT token to the Local DataStore to make subsequent requests
            saveToDataStore(loginJWTDto)
        }
    }

    fun findUserByEmail(email: String) {
        viewModelScope.launch {
            //Send request to find out user by email
            val fetchedUser = RetrofitInstance.api.getUserByEmail(email)
            //Save signed in user data
            _user.value = fetchedUser
        }
    }

    private fun saveToDataStore(loginJWTDto: LoginJWTDto) {
        viewModelScope.launch(Dispatchers.IO) {
            //Saves JWT token and email to the database
            repository.saveUserToken(loginJWTDto)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.clearUserCredentials()
        }
    }
}