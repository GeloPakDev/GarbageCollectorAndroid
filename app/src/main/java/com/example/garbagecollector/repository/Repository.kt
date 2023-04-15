package com.example.garbagecollector.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoterDataSource: RemoteDataSource,
    localDataStore: LocalDataStore
) {
    val remoteDataSource = remoterDataSource
    val localDataStore = localDataStore
}