package com.example.garbagecollector.di

import android.content.Context
import androidx.room.Room
import com.example.garbagecollector.repository.database.GarbageCollectorDatabase
import com.example.garbagecollector.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            GarbageCollectorDatabase::class.java,
            DATABASE_NAME,
        ).build()

    @Singleton
    @Provides
    fun provideLocationDao(database: GarbageCollectorDatabase) = database.locationDao()

}