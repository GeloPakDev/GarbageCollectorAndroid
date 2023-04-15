package com.example.garbagecollector.di

import android.content.Context
import com.example.garbagecollector.repository.local.DataStoreManager
import com.example.garbagecollector.repository.web.api.LocationApi
import com.example.garbagecollector.repository.web.api.UserApi
import com.example.garbagecollector.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideTokenFlow(@ApplicationContext context: Context): Flow<String> {
        val dataStoreManager = DataStoreManager(context)
        return dataStoreManager.userTokenFlow
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        userTokenFlow: Flow<String>
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = runBlocking { userTokenFlow.first() }
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Authorization", "Bearer $token")
                }.build())
            }.also { client ->
                client.addInterceptor(httpLoggingInterceptor)
            }.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()

    }

    @Singleton
    @Provides
    fun providesLocationApiService(retrofit: Retrofit): LocationApi {
        return retrofit.create(LocationApi::class.java)
    }

    @Singleton
    @Provides
    fun providesUserApiService(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
}