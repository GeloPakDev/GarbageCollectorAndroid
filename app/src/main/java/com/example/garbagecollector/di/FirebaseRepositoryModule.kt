package com.example.garbagecollector.di

import com.example.garbagecollector.repository.web.AuthRepository
import com.example.garbagecollector.repository.web.UserRepository
import com.example.garbagecollector.repository.web.impl.AuthRepositoryImpl
import com.example.garbagecollector.repository.web.impl.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseRepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        database: FirebaseAuth,
        fireStore: FirebaseFirestore,
        storage: FirebaseStorage
    ): AuthRepository {
        return AuthRepositoryImpl(database, fireStore, storage)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        database: FirebaseAuth,
        fireStore: FirebaseFirestore,
        storage: FirebaseStorage
    ): UserRepository {
        return UserRepositoryImpl(database, fireStore, storage)
    }
}