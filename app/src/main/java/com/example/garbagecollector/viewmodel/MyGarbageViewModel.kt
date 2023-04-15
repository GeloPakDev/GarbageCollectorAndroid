package com.example.garbagecollector.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.garbagecollector.repository.web.api.LocationApi
import com.example.garbagecollector.repository.web.dto.LocationDto
import com.example.garbagecollector.repository.local.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyGarbageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DataStoreManager(application)
    val token = repository.userTokenFlow.asLiveData()
    val userId = repository.userId.asLiveData()



    suspend fun getClaimedUserLocations(userId: Long): List<LocationDto> =
        withContext(Dispatchers.IO)
        {
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
                .create(LocationApi::class.java)

            retrofit.getClaimedUserLocations(userId)
        }

    suspend fun getPostedUserLocations(userId: Long): List<LocationDto> =
        withContext(Dispatchers.IO)
        {
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
                .create(LocationApi::class.java)

            retrofit.getPostedUserLocations(userId)
        }
}