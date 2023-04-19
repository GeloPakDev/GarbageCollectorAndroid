package com.example.garbagecollector.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataStore: LocalDataStore
)