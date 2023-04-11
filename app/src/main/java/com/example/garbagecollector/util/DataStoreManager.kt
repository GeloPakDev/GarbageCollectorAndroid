package com.example.garbagecollector.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.garbagecollector.api.dto.LoginJWTDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    companion object {
        val AUTH_TOKEN = stringPreferencesKey("JWT_TOKEN")
        val USER_EMAIL = stringPreferencesKey("USER_EMAIL")
        val USER_ID = longPreferencesKey("USER_ID")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = Constants.DATASTORE_NAME
        )
    }


    val userTokenFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN] ?: ""
    }

    val userEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL] ?: ""
    }

    val userId: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[USER_ID] ?: 0L
    }

    suspend fun saveUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun saveUserCredentials(loginJWTDto: LoginJWTDto) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = loginJWTDto.token.toString()
            preferences[USER_EMAIL] = loginJWTDto.email.toString()
        }
    }

    suspend fun clearUserCredentials() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
            preferences.remove(USER_EMAIL)
        }
    }
}