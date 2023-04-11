package com.example.garbagecollector.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.location.Address
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.garbagecollector.api.LocationApi
import com.example.garbagecollector.api.RetrofitInstance
import com.example.garbagecollector.api.dto.LocationDto
import com.example.garbagecollector.model.State
import com.example.garbagecollector.util.DataStoreManager
import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.time.LocalDate


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DataStoreManager(application)
    val token = repository.userTokenFlow.asLiveData()
    val userId = repository.userId.asLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addLocation(latLng: LatLng, address: Address?, garbagePhoto: Bitmap) {
        val location = LocationDto()
        location.name = address?.featureName ?: ""
        location.city = address?.locality ?: ""
        location.postalCode = address?.postalCode?.toInt() ?: 0
        location.latitude = latLng.latitude
        location.longitude = latLng.longitude
        //Convert the Bitmap to byte array
        val stream = ByteArrayOutputStream()
        garbagePhoto.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        // encode the byte array to a base64 string
        location.photo = Base64.encodeToString(byteArray, Base64.DEFAULT)
        location.state = State.NEW
        location.creationDate = LocalDate.now().toString()
        location.claimDate = null
        location.postedUserId = userId.value
        location.claimedUserId = null

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

        retrofit.postLocation(location)
    }

    suspend fun claimLocation(locationId: Long, claimedUserId: Long) {
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
        retrofit.claimLocation(locationId, claimedUserId)
    }

    suspend fun getAllLiveLocations(): MutableLiveData<List<LocationDto>> {
        return MutableLiveData(RetrofitInstance.locationApi.getLocations())
    }
}