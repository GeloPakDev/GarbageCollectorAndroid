package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.garbagecollector.repository.web.RetrofitInstance
import com.example.garbagecollector.repository.web.api.UserApi
import com.example.garbagecollector.repository.web.dto.LoginDto
import com.example.garbagecollector.repository.web.dto.LoginJWTDto
import com.example.garbagecollector.repository.web.dto.RegistrationDto
import com.example.garbagecollector.repository.web.dto.UserDto

import com.example.garbagecollector.repository.local.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DataStoreManager(application)
    val token = repository.userTokenFlow.asLiveData()
    val email = repository.userEmail.asLiveData()


    fun registerUser(registrationDto: RegistrationDto) {
        viewModelScope.launch(Dispatchers.IO) {
            //Send request to register the user
            RetrofitInstance.userApi.registerUser(registrationDto)
        }
    }

    fun loginUser(loginDto: LoginDto) {
        viewModelScope.launch(Dispatchers.IO) {
            //Send request to sign in user and retrieve JWT token and email
            val loginJWTDto = RetrofitInstance.userApi.loginUser(loginDto)
            //Save JWT token to the Local DataStore to make subsequent requests
            saveCredentialsToDataStore(loginJWTDto)
        }
    }

    suspend fun findUserByEmail(email: String): UserDto =
        withContext(Dispatchers.IO) {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(chain.request().newBuilder().also {
                        it.addHeader("Authorization", "Bearer ${token.value}")
                    }.build())
                }.also { client ->
                    val logging =
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }.build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://garbagecollectorapp.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(UserApi::class.java)
            //Send request to find out user by email
            val user = retrofit.getUserByEmail(email)
            user.data?.id?.let { saveUserIdToDataStore(it) }
            user
        }


    private fun saveCredentialsToDataStore(loginJWTDto: LoginJWTDto) {
        viewModelScope.launch(Dispatchers.IO) {
            //Saves JWT token and email to the database
            repository.saveUserCredentials(loginJWTDto)
        }
    }

    private fun saveUserIdToDataStore(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUserId(userId)
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearUserCredentials()
        }
    }
}